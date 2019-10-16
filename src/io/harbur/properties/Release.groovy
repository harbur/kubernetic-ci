package io.harbur.properties

class Release {
    def name
    def namespace
    def path
    List<String> values

    Release(yaml) {
        this.name = yaml.name
        this.namespace = yaml.namespace
        this.path = yaml.path
        this.values = yaml.values
    }
}