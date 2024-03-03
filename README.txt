The implementation in this master thesis consists of three Java programs, a few scripts, and a few dockerfiles. Then, there is a file (data-for-testbed) where all the data used in the testbed is stored. 

Each Java program folder contains an explanation file in which the classes in that Java program are explained in detail. These three Java programs, and their tasks are briefly as follows:

Path: /master-thesis-ozcankaraca/
java-program-for-testbed: This java program implements the files and data required to set up the testbed. 

java-programme-for-container: This java program is used to communicate with each other after the containers are created. 

java-program-for-validation: This java program is used to verify the accuracy of the data transferred after the data transfer process is finished, verify the network characteristics configured between the containers, and calculate the entire testbed time.

During the creation of Containerlab, the following two scripts were used to set up the connections between the containers and the network characteristics. 

Path: /master-thesis-ozcankaraca/data-for-testbed/data-for-connection/script-for-connection/
connection-details-peer: This script determines how a peer container should behave and which IP Address and network interface should have. 

connection-details-super peer: This script makes all settings for lectureStudio-server and super-peers. These settings are the different IP addresses and network interfaces they have. In addition, for the connections they have with all peers, bandwidth delay and packet loss adjustments are provided with this script.

In total, 4 Docker images (image-testbed, image-tracker, image-cadvisor and image-prometheus) were used: a YML file for Prometheus features and a yml for Docker-compose (for Grafana). 
In order for a test to be successfully started and the necessary time measurements to be made, two images must be created: image-tracker, image-testbed

image-testbed:
Path: /master-thesis-ozcankaraca/java-program-for-container/
docker build -f dockerfile.testbed -t image-testbed .

image-tracker
Path: /master-thesis-ozcankaraca/java-program-for-validation/
docker build -f dockerfile.tracker -t image-tracker .

image-cadvisor:
Path: /master-thesis-ozcankaraca/data-for-testbed/data-for-analysing-monitoring/
docker build -f dockerfile.cadvisor -t image-cadvisor .

image-cadvisor:
Path: /master-thesis-ozcankaraca/data-for-testbed/data-for-analysing-monitoring/
docker build -f dockerfile.prometheus -t image-prometheus .

To run the Testbed:

A total of two scripts are used to configure and then run the testbed. These are as follows:

Path: /master-thesis-ozcankaraca/
configuration-testbed: This is where all the Java programs, images, and scripts described above are configured. This script configures and runs the entire testbed according to three values from the run-testbed script.

run-testbed: This is the home directory of the testbed and the script file where three main values as arrays are entered. These three main values are as follows:

1 - Enter whether the P2P algorithm is used or not. (p2p_algorithm_used_values)
2 - The number of peers that should be used in the testbed, excluding lectureStudio-server, tracker-peer, and analysis and display peers. (number_of_peers_values)
3 - Enter the file size to be used for data transfer in the testbed. (choice_of_pdf_mb_values)--> The important thing here is that there must be PDF data to be sent under this folder (Path: /master-thesis-ozcankaraca/data-for-testbed/data-for-tests/pdf-files/), and the name of the pdf must be named here, along with its size. For example, 4MB.pdf

After running the testbed, the results are going to be stored in a CSV file:
Path: /master-thesis-ozcankaraca/results/results-testbed.csv

