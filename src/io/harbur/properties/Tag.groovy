package io.harbur.properties

class Tag {
    def name

    Tag(name) {
        this.name = name
    }

    def getFormattedName(def script) {
        return script.sh(script: "echo -n ${this.name}", returnStdout: true).replaceAll('/','.')
    }
}