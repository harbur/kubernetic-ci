#!/usr/bin/groovy
package io.harbur.cmds

def build() {
  def properties = new io.harbur.utils.Properties()
  def registry = properties.global().registry

  for (config in properties.project().docker) {

    echo "Authenticating to Registry ${registry}"
    docker.withRegistry(registry, 'gcr:woven-computing-234012') {
      def customImage = docker.build("${config.image}", "-f ${config.path} ${config.context}")

      if (config.tags) {
        for (tag in config.tags) {
          tag = sh(script: "echo -n ${tag}", returnStdout: true).replaceAll('/','.')
          customImage.push(tag)
        }
      }
    }
  }
}
