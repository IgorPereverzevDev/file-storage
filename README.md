# Road-Map File-Storage

# Overview
Design and implement REST interface to consume data-snapshots from one client, validate and persist data in storage, distribute persisted data to other clients via REST interface.

# Description
1.	Client can upload plain text file with comma-separated data via HTTP request

    a. First line of file will contain header: PRIMARY_KEY,NAME,DESCRIPTION,UPDATED_TIMESTAMP
    
    b. Last line of file always to be empty
    
    c. All other lines will contain four values what represents single record to be persisted
    
2.	Client can access data persisted via HTTP request

    a.Values of single record to be provided for PRIMARY_KEY supplied via request URL
    
    b.Paginated list of records at specific time period
    
3.	Service owner can remove record from storage via HTTP request by single PRIMARY_KEY for reconciliation purpose

4.	Service owner can prevent persistence of all records from client-file what contains invalid rows

# Maven
   
    Build the project:

    $ mvn package


# API

Upload file
http://localhost:8080/upload

Get Record
http://localhost:8080/record

Get Records
http://localhost:8080/records

Remove Record
http://localhost:8080/remove


# Request and Response



