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
## Upload file

Request

     key: file
     value: textfile.txt (get from the repository)
Response

    Your file was uploaded successfully

## Get Record

Request example
        
     key: key
     value: aaa
Response example

    {
        "id": 1,
        "key": "aaa",
        "name": "name",
        "description": "description",
        "updatedTimeStamp": "2020-09-20T12:54:00.121+00:00"
    }


## Get Records

Request example
        
     timeFrom: 2020-09-20 14:54:00.121
     timeTo: 2020-09-20 14:54:00.121
Response example


     {
   
        "content": [
    
        {
            "id": 1,
            "key": "aaa",
            "name": "name",
            "description": "description",
            "updatedTimeStamp": "2020-09-20T12:54:00.121+00:00"
        }
        
    ],
    
    "pageable": {
        "sort": {
            "sorted": false,
            "unsorted": true,
            "empty": true
        },
        "offset": 0,
        "pageNumber": 0,
        "pageSize": 10,
        "paged": true,
        "unpaged": false
    },
    
    "totalPages": 1,
    "totalElements": 1,
    "last": true,
    "size": 10,
    "number": 0,
    "sort": {
        "sorted": false,
        "unsorted": true,
        "empty": true
    },
        "numberOfElements": 1,
        "first": true,
        "empty": false
    }
    

## Remove Record

Request example
        
     key: key
     value: aaa
     
Response example

    Record removed
    

#   What can be improved 

    add roles for owner and client and access
    
    add integration testing 
    
    add datasource for performance and switch to native queries


