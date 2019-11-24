package io.harbur.cmds

import io.harbur.properties.Chart
import io.harbur.properties.Release
import io.harbur.properties.Repository

/**
 * Class for Helm commands.
 */
class Helm {

  /**
   * Initializes Helm client side.
   *
   * @param script Reference to script scope to access `sh()`
   */
  static def init(def script) {
    script.sh(
      script: "helm init -c",
      label: "Initializing helm")
  }

  /**
   * Adds Helm repositories client side.
   *
   * @param script Reference to script scope to access `sh()`
   * @param repos Repositories defined in global `properties.yaml`
   */
  static def addRepos(def script, List<Repository> repos) {
    for (repo in repos) {
      if (repo.username && repo.password) {
        script.wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: repo.password, var: 'REPO_PASSWORD']]]) {
          script.withEnv(["REPO_PASSWORD=${repo.password}"]) {
            script.sh(
              script: "helm repo add ${repo.name} --username ${repo.username} --password ${repo.password} ${repo.url}",
              label: "Adding helm repo: ${repo.name}")
          }
        }
      } else {
        script.sh(
          script: "helm repo add ${repo.name} ${repo.url}",
          label: "Adding helm repo: ${repo.name}")
      }
    }
  }

  /**
   * Packages Helm charts.
   *
   * @param script Reference to script scope to access `sh()`
   * @param charts Charts defined in project `kubernetic.yaml`
   */
  static def pack(def script, List<Chart> charts) {
    for (chart in charts) {
      script.sh(
        script: """
                  mkdir -p build/packages
                  helm dep build "${chart.name}"
                  helm package --destination build/packages/ "${chart.name}"
                """,
        label: "Packaging helm chart: ${chart.name}")
    }
  }

  /**
   * Tests Helm Charts.
   *
   * @param script Reference to script scope to access `sh()`
   * @param charts Charts defined in project `kubernetic.yaml`
   */
  static def test(def script, List<Chart> charts) {
    for (chart in charts) {
      script.sh(
        script: """
                  helm lint "${chart.name}"
                """,
        label: "Testing helm chart: ${chart.name}")
    }
  }

  /**
   * Pushes Helm Charts.
   *
   * @param script Reference to script scope to access `sh()`
   * @param charts Charts defined in project `kubernetic.yaml`
   */
  static def push(def script, def chartRepo) {
    script.sh(
      script: """
                for file in `ls -1 build/packages/*.tgz`; do
                  helm push \$file ${chartRepo}
                done
              """,
      label: "Pushing helm charts")
  }

  /**
   * Upgrades Releases.
   *
   * @param script Reference to script scope to access `sh()`
   * @param releases Releases defined in project `kubernetic.yaml`
   */
  static def upgrade(def script, List<Release> releases) {
    for (release in releases) {
      def params = ""
      if (release.values) {
        params+= "-f " + release.values.join(" -f ")
      }
      if (script.fileExists(file: "${release.path}/dependencies.yaml")) {
        script.sh(
          script: """
                  helm dep build ${release.path}
                  helm upgrade -i ${release.name} ${release.path} --namespace ${release.namespace} ${params}
                  """,
          label: "Upgrading helm release: ${release.name}")
      } else {
        script.sh(
          script: """
                  helm upgrade -i ${release.name} ${release.path} --namespace ${release.namespace} ${params}
                  """,
          label: "Upgrading helm release: ${release.name}")
      }
    }
  }
}