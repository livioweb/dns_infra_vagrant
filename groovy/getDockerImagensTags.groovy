import groovy.json.JsonSlurper
docker_image_tags_url = "https://hub.docker.com/v2/repositories/liviorodrigues/aracnos/tags/?page_size=20"
try {
    def http_client = new URL(docker_image_tags_url).openConnection() as HttpURLConnection
    http_client.setRequestMethod('GET')
    http_client.connect()
    def dockerhub_response = [:]    
    if (http_client.responseCode == 200) {
        dockerhub_response = new JsonSlurper().parseText(http_client.inputStream.getText('UTF-8'))
    } else {
        println("HTTP response error")
        System.exit(0)
    }
    def image_tag_list = []
    dockerhub_response.results.each { tag_metadata ->
        image_tag_list.add("Aracnos " + tag_metadata.name)    
    }
    return image_tag_list.sort()
} catch (Exception e) {
         println(e)
}