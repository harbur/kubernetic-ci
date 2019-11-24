package io.harbur.cmds

import io.harbur.properties.Image
import io.harbur.properties.Registry

/**
 * Class for Docker commands.
 */
class Docker {

  /**
   * Builds and pushes the docker all images defined in project `kubernetic.yaml`.
   * Once an Image is built, it is pushed with all tags defined.
   * Tags are evaluated with environment variables, e.g. a tag can be `${GIT_BRANCH}`.
   *
   * @param docker Reference to docker plugin
   * @param script Reference to script scope to access `sh()`
   * @param images Images defined in project `kubernetic.yaml`
   * @param registry Registry defined in global `properties.yaml`
   */
  static def build(org.jenkinsci.plugins.docker.workflow.Docker docker, def script, List<Image> images, Registry registry) {
    for (image in images) {
      docker.withRegistry(registry.url, registry.credentialsId) {
        // Build Image
        def imageName = image.getName(script, registry)
        def customImage = docker.build("${imageName}", "-f ${image.path} ${image.context}")

        // Push Image
        if (image.tags) {
          for (tag in image.tags) {
            def imageTag = tag.getFormattedName(script)
            customImage.push(imageTag)
          }
        }
      }
    }
  }
}