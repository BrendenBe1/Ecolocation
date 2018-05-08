import ast
import csv
import sys
from collections import defaultdict
import json
import mysql.connector

''' the purpose of this file is to reduce the polygon size if necessary. Only run this script on
    animals that don't go into the databse using the Input_WKT_to_Database.py file.
    This file reduces the

'''
def getSquareDistance(p1, p2):
    """
    Square distance between two points
    """
    dx = p1['x'] - p2['x']
    dy = p1['y'] - p2['y']

    return dx * dx + dy * dy


def getSquareSegmentDistance(p, p1, p2):
    """
    Square distance between point and a segment
    """
    x = p1['x']
    y = p1['y']

    dx = p2['x'] - x
    dy = p2['y'] - y

    if dx != 0 or dy != 0:
        t = ((p['x'] - x) * dx + (p['y'] - y) * dy) / (dx * dx + dy * dy)

        if t > 1:
            x = p2['x']
            y = p2['y']
        elif t > 0:
            x += dx * t
            y += dy * t

    dx = p['x'] - x
    dy = p['y'] - y

    return dx * dx + dy * dy


def simplifyRadialDistance(points, tolerance):
    length = len(points)
    prev_point = points[0]
    new_points = [prev_point]

    for i in range(length):
        point = points[i]

        if getSquareDistance(point, prev_point) > tolerance:
            new_points.append(point)
            prev_point = point

    if prev_point != point:
        new_points.append(point)

    return new_points


def simplifyDouglasPeucker(points, tolerance):
    length = len(points)
    markers = [0] * length  # Maybe not the most efficent way?

    first = 0
    last = length - 1

    first_stack = []
    last_stack = []

    new_points = []

    markers[first] = 1
    markers[last] = 1

    while last:
        max_sqdist = 0

        for i in range(first, last):
            sqdist = getSquareSegmentDistance(points[i], points[first], points[last])

            if sqdist > max_sqdist:
                index = i
                max_sqdist = sqdist

        if max_sqdist > tolerance:
            markers[index] = 1

            first_stack.append(first)
            last_stack.append(index)

            first_stack.append(index)
            last_stack.append(last)

        # Can pop an empty array in Javascript, but not Python, so check
        # the length of the list first
        if len(first_stack) == 0:
            first = None
        else:
            first = first_stack.pop()

        if len(last_stack) == 0:
            last = None
        else:
            last = last_stack.pop()

    for i in range(length):
        if markers[i]:
            new_points.append(points[i])

    return new_points


def simplify(points, tolerance=0.1, highestQuality=True):
    sqtolerance = tolerance * tolerance

    if not highestQuality:
        points = simplifyRadialDistance(points, sqtolerance)

    points = simplifyDouglasPeucker(points, sqtolerance)

    return points


def geom_to_str(test_data):
    x = "["
    for i in range(0, len(test_data), 2):
        # print(test_data[i], ",", test_data[i+1])
        x += '{"x":' + str(test_data[i]) + ', "y":' + str(test_data[i + 1]) + '},'

    x += "]"
    y = ast.literal_eval(x)
    return y


def dict_to_geom(reduced_poly):
    poly_str = '('
    for i in range(0, len(reduced_poly)):
        if i+1 != len(reduced_poly):
            poly_str += str(reduced_poly[i]['x']) + ' ' + str(reduced_poly[i]['y']) + ','
        else:
            poly_str += str(reduced_poly[i]['x']) + ' ' + str(reduced_poly[i]['y'])
    poly_str += ')'
    #print(poly_str)

    return poly_str

def dict_to_geom_multi(reduced_poly):
    poly_str = '(('
    for i in range(0, len(reduced_poly)):
        if i+1 != len(reduced_poly):
            poly_str += str(reduced_poly[i]['x']) + ' ' + str(reduced_poly[i]['y']) + ','
        else:
            poly_str += str(reduced_poly[i]['x']) + ' ' + str(reduced_poly[i]['y'])
    poly_str += '))'
    #print(poly_str)

    return poly_str


def run_reduction(wkt):
    if wkt[0] == 'P':
        final_string = "POLYGON ("
        wkt = wkt[8:]
        #print(wkt)
        wkt = wkt.replace("((", '[[').replace("))", ']]').replace(' ', ',').replace("(", '[').replace(
            ")", ']')
        #wkt = ast.literal_eval(wkt)
        #print(wkt)
        json_test = wkt.replace("'", "\"")
        wkt = json.loads(json_test)
        for i in range(0, len(wkt)):
            reduce_input = geom_to_str(wkt[i])
            output = simplify(reduce_input, .02, True)
            print(len(reduce_input), len(output))
            final_string += dict_to_geom(output)
            if i + 1 != len(wkt):
                final_string += "," # need comma between polygons
        final_string += ')'
        print(final_string)
    else:
        final_string = "MULTIPOLYGON ("
        wkt = wkt[13:]
        wkt = wkt.replace("(((", '[[').replace(")))", ']]').replace(' ', ',').replace("((", '[').replace(
            "))", ']').replace("(", '[').replace(
            ")",   ']')
        json_test = wkt.replace("'", "\"")
        wkt = json.loads(json_test)
        for i in range(0, len(wkt)):
            reduce_input = geom_to_str(wkt[i])
            output = simplify(reduce_input, .02, True)
            print(len(reduce_input), len(output))
            final_string += dict_to_geom_multi(output)
            if i + 1 != len(wkt):
                final_string += "," # need comma between polygons

        final_string += ')'
        print(final_string)
    return final_string

############################################################################################################################
def add_entry(animal_name):
    '''cnx = mysql.connector.connect(user='root', password='',
                                      host='127.0.0.1',
                                      database='ecolocation_data')'''

    cnx = mysql.connector.connect(user="TeamEcolocation", password="EcolocationData",
                                  host="ecolocation.c09lpapromur.us-east-2.rds.amazonaws.com", port = 3306,
                                  database="ecolocation_data")

    cursor = cnx.cursor()

    maxInt = sys.maxsize
    decrement = True

    while decrement:
        # decrease the maxInt value by factor 10
        # as long as the OverflowError occurs.

        decrement = False
        try:
            csv.field_size_limit(maxInt)
        except OverflowError:
            maxInt = int(maxInt / 10)
            decrement = True

    csv.field_size_limit(maxInt)

    binomial_read = []
    shape_length_read = []
    shape_area_read = []
    wkt_read = []
    # print(binomial2[j])

    new_csvfile = "wkt/" + animal_name + ".csv"

    columns = defaultdict(list)  # each value in each column is appended to a list
    try:
        with open(new_csvfile) as f:
            reader = csv.DictReader(f)  # read rows into a dictionary format
            for row in reader:  # read a row as {column1: value1, column2: value2,...}
                for (k, v) in row.items():  # go over each column name and value
                    columns[k].append(v)  # append the value into the appropriate list
                    # based on column name k

        for i in range(0, len(columns['binomial'])):
            binomial_read.append(columns['binomial'][i])
            shape_length_read.append(columns['shape_Leng'][i])
            shape_area_read.append(columns['shape_Area'][i])
            wkt_read.append(columns['WKT'][i])

        for i in range(0, len(binomial_read)):
            #print(binomial_read[i])
            new_wkt = run_reduction(str(wkt_read[i]))
            statement = "UPDATE iucn SET boundaries = GeomFromText(%s) WHERE binomial = %s AND (shape_leng = %s OR shape_area = %s)"  # start here tomorrow with the OR
            info = (new_wkt, binomial_read[i], round(float(shape_length_read[i]), 4), round(float(shape_area_read[i]), 4))
            # cursor.execute(statement, (name[i],))
            try:
                cursor.execute(statement, info)
                cnx.commit()
                print(binomial_read[i], len(wkt_read[i]))
            except:
                print("failed: ", binomial_read[i], round(float(shape_length_read[i]), 4), round(float(shape_area_read[i]), 4))
                continue
    except:
        print(new_csvfile)


    cnx.close()
    cursor.close()

    #for j in range(0, len(wkt_read)):
        #run_reduction(str(wkt_read[0]))
    #print(wkt_read[0])
############################################################################################################################

#run_reduction(more_test)
#add_entry('Panthera pardus') # the white whale
test_list = ['Deltamys kempi', 'Hadromys yunnanensis', 'Rhynchocyon cirnei', 'Marmosops impavidus', 'Martes martes', 'Crocidura lamottei', 'Sorex pacificus', 'Mirza zaza', 'Brucepattersonius albinasus', 'Blanfordimys afghanus', 'Burramys parvus']
for k in range(0, len(test_list)):
    add_entry(test_list[k])
