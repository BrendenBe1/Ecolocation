import csv
from collections import defaultdict
import mysql.connector


def take_3():
    cnx = mysql.connector.connect(user='team_ecolocation', password='Ecolocation',
                                  host='ecolocationdata.c8qsf4w8dkdu.us-east-2.rds.amazonaws.com',
                                  database='animal_data')

    cursor = cnx.cursor()

    '''cursor.execute("""
          select * from iucn
       """)
    result = cursor.fetchall()
    print(result)'''

    columns = defaultdict(list)  # each value in each column is appended to a list

    with open('animal_coordinates.csv') as f:
        reader = csv.DictReader(f)  # read rows into a dictionary format
        for row in reader:  # read a row as {column1: value1, column2: value2,...}
            for (k, v) in row.items():  # go over each column name and value
                columns[k].append(v)  # append the value into the appropriate list
                # based on column name k
    lon_min = []
    lon_max = []
    lat_min = []
    lat_max = []
    name = []
    for i in range(0,len(columns['name'])):
        name.append(columns['name'][i])

    for i in range(0,len(columns['lon_min'])):
        lon_min.append(float(columns['lon_min'][i]))

    for i in range(0,len(columns['lon_max'])):
        lon_max.append(float(columns['lon_max'][i]))

    for i in range(0,len(columns['lat_min'])):
        lat_min.append(float(columns['lat_min'][i]))

    for i in range(0,len(columns['lat_max'])):
        id_no = i + 6
        lat_max.append(float(columns['lat_max'][i]))
        statement = "INSERT iucn (id, binomial, lat_min, lat_max, lon_min, lon_max) VALUES(%s, %s, %s, %s, %s, %s)"
        info = (id_no, name[i], lat_min[i], lat_max[i], lon_min[i], lon_max[i])
        cursor.execute(statement, info)
        cnx.commit()
        '''print(columns['name'][i])
        print(float(columns['lon_min'][i]))
        print(float(columns['lon_max'][i]))
        print(float(columns['lat_min'][i]))
        print(float(columns['lat_max'][i]))'''

    lon_minl = list(set(lon_min))
    lon_maxl = list(set(lon_max))
    lat_minl = list(set(lat_min))
    lat_maxl = list(set(lat_max))
    namel = list(set(name))

    '''statement = "INSERT iucn (id, binomial, lat_min, lat_max, lon_min, lon_max) VALUES(%s, %s, %s, %s, %s, %s)"
    info = (12, name[5], lat_min[5], lat_max[5], lon_min[5], lon_max[5])
    cursor.execute(statement, info)
    cnx.commit()'''

    cnx.close()

take_3()