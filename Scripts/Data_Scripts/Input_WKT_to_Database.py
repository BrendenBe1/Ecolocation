import csv
import mysql.connector
from collections import defaultdict
import sys

maxInt = sys.maxsize
decrement = True

while decrement:
    # decrease the maxInt value by factor 10
    # as long as the OverflowError occurs.

    decrement = False
    try:
        csv.field_size_limit(maxInt)
    except OverflowError:
        maxInt = int(maxInt/10)
        decrement = True

csv.field_size_limit(maxInt)


def add_mass():
    '''cnx = mysql.connector.connect(user='root', password='',
                                  host='127.0.0.1',
                                  database='ecolocation_data')'''
    cnx = mysql.connector.connect(user="TeamEcolocation", password="EcolocationData",
                                  host="ecolocation.c09lpapromur.us-east-2.rds.amazonaws.com", port=3306,
                                  database="ecolocation_data")

    cursor = cnx.cursor()

    columns = defaultdict(list)  # each value in each column is appended to a list
    with open('wkt_missing.csv', encoding="utf8") as f:
        reader = csv.DictReader(f)  # read rows into a dictionary format
        for row in reader:  # read a row as {column1: value1, column2: value2,...}
            for (k, v) in row.items():  # go over each column name and value
                columns[k].append(v)  # append the value into the appropriate list
                # based on column name k
    binomial = []
    for i in range(0, len(columns['binomial'])):
        binomial.append(columns['binomial'][i])

    binomial = list(set(binomial))
    f.close()
    print(len(binomial))

    for i in range(0, len(binomial)):
    #for i in range(0, 5):
        # print(flist[i])
        new_csvfile = "wkt/" + binomial[i] + ".csv"
        #print(new_csvfile)
        try:
            with open(new_csvfile) as fi:
                reader = csv.DictReader(fi)  # read rows into a dictionary format
                for row in reader:  # read a row as {column1: value1, column2: value2,...}
                    print(row['binomial'], round(float(row['shape_Leng']), 4), round(float(row['shape_Area']), 4), row['WKT'])
                    statement = "UPDATE iucn SET boundaries = GeomFromText(%s) WHERE binomial = %s AND (shape_leng = %s OR shape_area = %s)" # start here tomorrow with the OR
                    info = (row['WKT'], binomial[i], round(float(row['shape_Leng']), 4), round(float(row['shape_Area']), 4))
                    # cursor.execute(statement, (name[i],))
                    cursor.execute(statement, info)
                    cnx.commit()
                fi.close()
        except:
           print("FAILED: ", binomial[i])

    cnx.close()
    cursor.close()


    #with open("new_wkt.csv", 'w') as wr:

    '''with open('iucn_complete.csv', encoding="utf8") as f:
        reader = csv.DictReader(f)  # read rows into a dictionary format
        for row in reader:  # read a row as {column1: value1, column2: value2,...}
            if row['binomial'] in mass:'''


add_mass()