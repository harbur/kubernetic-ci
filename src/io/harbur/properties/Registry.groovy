package io.harbur.properties

class Registry {
    def url
    def name
    def credentialsId

    Registry(yaml) {
        this.url = yaml.url
        this.name = yaml.name
        this.credentialsId = yaml.credentialsId
    }
}