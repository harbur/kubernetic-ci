package io.harbur.properties

import io.harbur.properties.Tag
import io.harbur.properties.Registry

class Image {
    def name
    def path
    def context
    List<Tag> tags

    Image(yaml) {
        this.name = yaml.name
        this.path = yaml.path
        this.context = yaml.context
        if (yaml.tags) {
            this.tags = yaml.tags.collect { name ->
                return new Tag(name)
            }
        } else {
            this.tags = [new Tag('${GIT_BRANCH}')]
        }
    }

    def getName(def script, Registry registry) {
        if (name) {
            return name
        }
        def baseName = 'harbur/${PROJECT_NAME}}'
        return script.sh(script: "echo -n ${registry.name}/${baseName}", returnStdout: true)
    }
}