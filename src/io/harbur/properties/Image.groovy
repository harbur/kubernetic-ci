package io.harbur.properties

import io.harbur.properties.Tag

class Image {
    def name
    def path
    def context
    List<Tag> tags

    Image(yaml) {          
        this.name = yaml.name
        this.path = yaml.path
        this.context = yaml.context
        this.tags = yaml.tags.collect { name ->
            return new Tag(name)
        }
    }
}