import csv
import wikipedia
import sys

def getInfo(name):
    """
    Gets the page for a specific animals and returns the needed info.
    The info being retrieved is: common name (page title), description (three sentences), and the wikipedia link

    @:param name:   the scientific name of the animal to retrieve the wiki page for
    @:return:       A dictionary containing the common name, scientific name, url, and description of the animal given.
                    The format of the dictionary is: {'binomial': scientific_name, 'common_name': common_name,
                    'wiki_link': url, 'desc': description} where all values are strings
    @:except TimeoutError:
    @:except: anything else
    """
    try:
        page = wikipedia.page(name)
    except TimeoutError:
        print('Could not get page for: %s\n' %(name))
        return {'binomial': name, 'common_name': name, 'wiki_link': '', 'desc': ''}
    except:
        print('Could not get page for: %s\n' %(name))
        return {'binomial': name, 'common_name': name, 'wiki_link': '', 'desc': ''}

    try:
        # retrieve title (common name), summary (desc), and link (wiki_link)
        description = wikipedia.summary(name, sentences=3)
        return {'binomial': name, 'common_name': page.title, 'wiki_link': page.url, 'desc': description}
    except TimeoutError as e:
        print('Could not get summary for: %s\n' %(name))
        return {'binomial': name, 'common_name': page.title, 'wiki_link': page.url, 'desc': ''}

    except:
        print('Could not get summary for: %s \n' % (name))
        return {'binomial': name, 'common_name': page.title, 'wiki_link': page.url, 'desc': ''}


    # put into a dictionary
    return {'binomial': name, 'common_name': page.title, 'wiki_link': page.url, 'desc': description}


def getAllInfo():
    """
    This is the script for obtain the information for all the animals. It reads from a csv file (iucn.csv) and writes combines the
    current information with the new information into a new csv file (iucn_complete.csv).

    URL's I used to help me with the reading and writing to csv files:
        https://stackoverflow.com/questions/11070527/how-to-add-a-new-column-to-a-csv-file
        http://www.pythonforbeginners.com/systems-programming/using-the-csv-module-in-python/

    @:param file:       the file name of the csv to read from. Do not include the '.csv' extension.
    @:param newFile:    the file name of the csv to create. Do not include the '.csv' extension.
    @:except UnicodeError:
    """
    #open file iucn.csv file and create a new file that will contain the appended info & the corresponding readers & writers
    fileIn = open(sys.argv[1], 'r')
    fileOut = open(sys.argv[2], 'a', newline='')
    reader = csv.reader(fileIn)
    writer = csv.writer(fileOut)

    print('Opened reader and writer')

    #this keeps track of which row the reader is located at in the iucn.csv file.
    index = 0

    for row in reader:
        #The first row (row 0) contains the header
        if index == 0:
            row.append('common_name')
            row.append('wiki_link')
            row.append('description')
        else:
            print('Retreiving row %d, ID #: %s' % (index, row[0]))
            #get name & format it
            animal_name = row[0].lower()
            animal_name = animal_name.replace(" ", "_")

            #get info on specific animal from wikipedia
            animal_data = getInfo(animal_name)

            #append data to row the current row
            row.append(animal_data['common_name'])
            row.append(animal_data['wiki_link'])
            row.append(animal_data['desc'])

            try:
                #add row to the new file iucn_complete.csv
                writer.writerow(row)

            except UnicodeError as e:
                print('Unicode Error')
                print(e)
        index += 1

    fileIn.close
    fileOut.close()

getAllInfo()