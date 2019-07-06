package io.harbur.properties

class Registry {
    def url
    def credentialsId

    Registry(yaml) {
        this.url = yaml.url
        this.credentialsId = yaml.credentialsId
    }
}