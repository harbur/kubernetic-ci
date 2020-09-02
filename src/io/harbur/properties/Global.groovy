package io.harbur.properties

import io.harbur.properties.Registry
import io.harbur.properties.Repository

class Global {
    Registry registry
    def chartRepo
    List<Repository> repos

    Global(yaml) {
        this.registry = new Registry(yaml.publicRegistry)
        this.chartRepo = yaml.chartRepo
        this.repos = yaml.repos.collect { k -> new Repository(k) }
    }

    static Global load(def script) {
        return new Global(script.readYaml(file: '/pipeline/properties.yaml'))
    }
}