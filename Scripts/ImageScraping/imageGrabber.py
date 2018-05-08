import urllib.request as req
import requests
import csv
import PIL
from PIL import Image
from resizeimage import resizeimage




# where the files are saved and read in from
folder = input( "File path to save images:\n" )
readCSV = input( ".csv with animal names:\n" )



# the row in the csv where scientific and common names are located
sciNameRow = int( input( "Which row (0-index) is the scientific name in?\n" ) )
commonNameRow = int( input( "Which row (0-index) is the common name in?\n" ) )


# format the inputs

if folder[-1] != '/':
    folder += '/'

if '.csv' not in readCSV:
    readCSV += '.csv'








# download an image from a url to a specific file
def downloadImage(url, fname):
    with req.urlopen(url) as d, open(fname, "wb") as opfile:
        data = d.read()
        opfile.write(data)
    



# resize an image
def resize( file ):
    try:
        img = Image.open( file )
        if img.width > img.height:
            width = 250
            height = (img.height * width) // img.width
        else:
            height = 250
            width = (img.width * height) // img.height
                     
        img = img.resize( ( width, height ), Image.ANTIALIAS )
        img.save( file )
        img.close()
    except:
        pass
    
    



####################################################################################################


########    GOOGLE IMAGES CODE #####################################################################



## create an array containing only the info in html tags <>
def tagParse( htmlText ):
    tags = []
    string = ""
    i = 0
    while i < len( htmlText ):
        if htmlText[i] == '<':
            while htmlText[i] != '>':
                string += htmlText[i] 
                i += 1
            string += htmlText[i] 
            tags.append( string )
            string = ""
        else:
            i += 1

    return tags

# return the url from an image tag
def getUrlFromTag( tag ):
    i = 0
    parCount = 0
    url = ""

    # the url is in the second set of parentheses
    while i < len( tag ):
        if tag[i] == '"' and parCount == 2:
            i += 1
            parCount += 1
            while tag[i] != '"':
                url += tag[i]
                i += 1
        elif tag[i] == '"':
            parCount += 1
        i += 1
    return url
    
        
# download the first result of a google image search
def getGoogleImage( sciName ):
    # create the url
    searchName = sciName.replace( " ","+" ).lower()
    url = "https://www.google.com/search?tbm=isch&q=" + searchName

    # get the html tags from the url
    try:
        a = requests.get( url )
        b = a.text
        tags = tagParse( b )
        # if it is an image tag, get the image's url
        for tag in tags:
            if "img" in tag:
                IMGurl = getUrlFromTag( tag )
                break
        
        # create the file path and download
        sciName = sciName.lower().replace( " ", "-" )
        file = folder + sciName + ".png"
        downloadImage( IMGurl, file )
        resize( file )
    except: 
        return 0





#############################################################################################################

##################   Arkive Images Code   ####################################################################

# gets the url from the "image_src" html tag
def getURL( line ):
    imageURL = ""
    i = 34
    while( line[i] != '"' ):
        imageURL += line[i]
        i += 1
    return imageURL

        
    
# download an image from arkive's website
def getArkiveImage( scientificName, commonName ):

    # format the url
    commonName = commonName.replace( " ", "-" ).lower()
    scientificName = scientificName.replace( " ", "-" ).lower()
    url = "https://www.arkive.org/" + commonName + "/" + scientificName + "/"

    try:
        # get the page's html from url
        htmlText = req.urlopen( url )
    except:
        return 0

    # loop through the file for the line
    # b'    <link rel="image_src" href="https://........
    i = 0
    for line in htmlText:
        if '<link rel="image_src" href="' in str( line ):
            url = getURL( str( line ) )
            fileName = folder + scientificName + ".png"
            downloadImage( url, fileName )
        i += 1


#####################################################################################################################

############################    Runner Code #######################################################


## can change the readCSV and location to save images at the top of page
# open the csv
fileIn = open( readCSV, 'r' )
reader = csv.reader( fileIn )
print( 'Opened reader and writer' )


# loop through every animal, skipping duplicates, and get each image
# add failures to a list
i = 0
j = 0
prevAnimal = ""
animalFailure = []
prevAnimals = set()
for row in reader:
    sciName = row[sciNameRow]
    commonName = row[commonNameRow]    
    if i == 0:
        i += 1
    elif sciName not in prevAnimals and i < 50:
        prevAnimals.add( sciName )
        print( str( i ) + ":  " + sciName + ":   " + commonName)
        i += 1

        # tries arkive first, then google images if that fails
        # 0 means a failure
        if commonName:
            if getArkiveImage( sciName, commonName ) == 0:
                if getGoogleImage( sciName ) == 0:
                    animalFailure.append( sciName )
        elif getGoogleImage( sciName, number ) == 0:
            animalFailure.append( sciName )

        
        



fileIn.close()

# print the failures
print( '\n\n\n\n\n\n' )
print( 'There were  ' + str( len( animalFailure ) ) + '  failures' )
for animal in animalFailure:
    print( animal )

