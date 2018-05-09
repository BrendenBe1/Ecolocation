# split 1 shapefile into many (https://rfunctions.blogspot.com/2013/03/spatial-analysis-split-one-shapefile.html)
# this creates an individual shape file for each mammal in the original shapefile
library(sp)
library(rgdal)

data <- readOGR("PleistoceneMammals_wgsg1984.shp") # name of shape file that contains all the mammals
unique <- unique(data@data$binomial)
print(unique)


for (i in 1:length(unique)) {
  tmp <- data[data$binomial == unique[i], ] # data$binomial is list of binomials in data frame
  writeOGR(tmp, dsn=getwd(), unique[i], driver="ESRI Shapefile",
           overwrite_layer=TRUE)
} 