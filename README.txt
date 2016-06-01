 Implementation details :
 
 1) Integrated file cache .Please check filecache.package
 2) Integrated static thread pool.Please check threadpool.package.
 3) Implemented both thread per connection and thread per resource policy.Please check http.package
 4) Implemented Session base authentication by writing cookies to response and form based login.Please check authentication.package
 5) Implemented Dispatcher Servlet pattern with POJO classes(without J2EE libraries or actual sevlet container).Please check servlets.package
 6) Implemented login,signup,logout features with help of database connection.Please check dao.package
 7) Handling GET,POST,HEAD requests and writing proper headers to the response.Please check http.package and servlets.package
 8) Integrate Velocity Template engine to generate dynamic content.Please check ResponeHandler.class from http.package.
 
 
Project Directory Structure

MultithreadedServer 
│
├───bin
│   └───classes
│       ├───authentication
│       ├───dao
│       ├───domain
│       ├───filecache
│       ├───http
│       ├───server
│       ├───servlets
│       ├───session
│       └───threadpool
├───database
│   
├───lib
├───src
│   ├───authentication
│   ├───dao
│   ├───domain
│   ├───filecache
│   ├───http
│   ├───server
│   ├───servlets
│   ├───session
│   └───threadpool
└───WebContent
    ├───resources
    │   ├───css
    │   ├───fonts
    │   ├───img
    │   ├───js
    │   └───less
    └───velocity
    
    
 Steps to run :
 
 1. Now open a terminal in the project root directory and run the target clean from build.xml file present in the root folder
    by simply entering 'ant clean' in the terminal.This will change the .some files in lib folder to .jar files.
 
 2. Open a terminal in database folder and run the targets from build.xml present in the database folder.
    a)  ant start-hsqldb
    b) Open a new terminal in the same folder i.e Database folder and run the following targets.
       b1)ant load-hsqldb
       b2)ant show-hsqldb
       note : use ant shutdown-hsqldb to stop hsqldb after testing.
 3. Now open a terminal in the project root directory and run the build.xml file present in the root folder
    by simply entering 'ant' in the terminal.
    
 4. Now open the browser and go to localhost:8888. Please check on browser to better understand the implementation.
    Because I implemented session base authentication by writing cookies to the response and form based login & signup.
	
	NOTE : use username = srikanthvarma.vadapalli@gmail.com
	           password = lancer
			   
			   or you can sign up with your details and try login.
    
    

 
  Thank you.
 
    