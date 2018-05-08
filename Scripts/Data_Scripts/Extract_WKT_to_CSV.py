import ogr,csv,os

# This script opens the shape file for each mammal and extracts the WKT geometry, writing the data to a csv file.

rootdir = '/historic_wgsg1984'  # directory where shape files are located
enddir = 'historic_wkt_csv/'  # directory to save CSV files to
extensions = ('.shp')

file_list = []
shape_list = []
# create list of all mammals to extract data for
for subdir, dirs, files in os.walk(rootdir):
    for file in files:
        ext = os.path.splitext(file)[-1].lower()
        if ext in extensions:
            shpfile = os.path.join(subdir, file)
            shape_list.append(shpfile)
            csv_in = file.split(".")
            csvfile = csv_in[0] + ".csv"
            csvfile = enddir + csvfile
            file_list.append(csvfile)

flist = file_list
slist = shape_list

# go through all mammals, extract WKT, write CSV
for i in range(0,len(flist)):
    new_csvfile = flist[i]
    new_shpfile = slist[i]
    csvfile=open(new_csvfile,'w')   # open CSV
    ds=ogr.Open(new_shpfile)    # open shapefile 
    lyr=ds.GetLayer()

    # Get field names
    dfn=lyr.GetLayerDefn()
    nfields=dfn.GetFieldCount()
    fields=[]
    # go through all layers of the shape file
    for i in range(nfields):
        fields.append(dfn.GetFieldDefn(i).GetName())
    fields.append('WKT')    # add WKT column to CSV
    csvwriter = csv.DictWriter(csvfile, fields) # write CSV
    try:csvwriter.writeheader() #python 2.7+
    except:csvfile.write(','.join(fields)+'\n')

    # Write attributes and kml out to csv
    for feat in lyr:
        attributes=feat.items()
        geom=feat.GetGeometryRef()
        attributes['WKT']=geom.ExportToWkt()
        csvwriter.writerow(attributes)

    #clean up
    del csvwriter,lyr,ds
    csvfile.close()