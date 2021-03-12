def jsonSlurper = new groovy.json.JsonSlurper()

def get = new URL("https://hub.docker.com/v2/repositories/library/?page=1&page_size=15").openConnection() as HttpURLConnection;
get.setRequestProperty( 'Accept', 'application/json' )
def getRC = get.getResponseCode();
def body = get.getInputStream().getText();
def object = jsonSlurper.parseText(body)
println(getRC);

def process = "ls -l".execute()
process.in.eachLine { line ->
    println line
}

if(getRC.equals(200)) {
    object.results.forEach(){line->
        println line.description
    }
    /*for(item in object.results){
    println item['description']
    }*/
}