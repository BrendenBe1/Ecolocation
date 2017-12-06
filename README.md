# Ecolocation
Mobile Application for Animal Ecological Interactions

To run prototype:
	* Store a copy of the animals.php files (located in php folder) in the htdocs folder of xampp
	* Start up xampp
		- if do not have megafauna_data database create it
			- database name is "megafauna_data"
			- table is "iucn"
			- columns are "animal_id, genus, species, mass, endangered_level"
				CREATE TABLE `iucn` (
				`animal_id` int(11) NOT NULL,
				`genus` varchar(45) DEFAULT NULL,
				`species` varchar(45) DEFAULT NULL,
				`mass` int(11) DEFAULT NULL,
				`endangered_level` int(11) DEFAULT NULL,
				PRIMARY KEY (`animal_id`)
				) ENGINE=InnoDB DEFAULT CHARSET=latin1
			- populate columns with data. currently using "lynx rufis", "Ursus americanus", "Ovis canedensis", "Cervus canadensis", and "Urocvon cinereoargenteus"
	* In file "DatabaseDemoActivity" file around line 90 change the IP adress to that of your computer. (use ipconfig in cmd to find)
	* This should be everything for accessing DB
