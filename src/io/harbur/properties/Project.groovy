package io.harbur.properties

import io.harbur.properties.Image
import io.harbur.properties.Chart
import io.harbur.properties.Release

class Project {
    List<Image> images
    List<Chart> charts
    List<Release> releases

    Project(yaml) {
        this.images = yaml.images.collect { k -> new Image(k) }
        this.charts = yaml.charts.collect { k -> new Chart(k) }
        this.releases = yaml.releases.collect { k -> new Release(k) }
    }

    static Project load(def script) {
        return new Project(script.readYaml(file: 'kubernetic.yaml'))
    }
}