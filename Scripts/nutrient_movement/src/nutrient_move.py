import csv
import json


def create_weight_file():
    """
        This script is converting a csv into a json file
    """

    file_in = open('../data/historic_data.csv', 'r')
    reader = csv.reader(file_in)
    
    data = []
    land = create_land_array()

    row = 0
    for rowItem in reader:
        print('Printing latitude: %d' % row)
        # create latitude
        lat = row - 90
        lat *= -1

        if lat >90 or lat < -90:
            x=1

        #create list for the latitude
        col = 0
        
        for value in rowItem:
            # create longitude
            long = col - 180

            if long > 180 or long < -180:
                x = 2
            #check if coordinate is on land
            if long in land[lat]:
                value = float(value)

                # make dictionary of the weighted coordinate
                weighted_coordinate = {}
                weighted_coordinate['lat'] = lat
                weighted_coordinate['long'] = long
                weighted_coordinate['value'] = value

                # make json object and add it to data_orig list
                # weighted_coordinate_dict = json.dumps(weighted_coordinate)
                data.append(weighted_coordinate)
            
            col += 1
        # write complete row to file

        row += 1

    file_in.close()

    with open('data_historic.json', 'w') as file_out:
        json.dump(data, file_out)
    file_out.close()


def create_land_array():
    file_in = open('../data_orig/land.csv', 'r')
    reader = csv.reader(file_in)

    land = dict()
    row = 0
    for row_item in reader:
        if row == 179:
            x=1

        lat = row - 90
        land[lat] = list()
        col = 0
        for col_item in row_item:
            long = col - 180
            col += 1

            isLand = int(col_item)
            if isLand == 1:
                land[lat].append(long)
            else:
                continue


        row += 1

    return land

create_weight_file()