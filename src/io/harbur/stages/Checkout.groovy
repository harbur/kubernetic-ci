package io.harbur.stages

import io.harbur.cmds.Git

/**
 * Class for Checkout stages.
 */
class Checkout {

  /**
   * Runs the following stages:
   *
   * - "Checkout": It clones the Git repository and exposes SCM variables as environment variables.
   *
   * @param script Reference to script scope.
   */
  static def run(def script) {
    checkout(script)
  }

  private static def checkout(def script) {
    script.stage ('Checkout') {
      Git.checkout(script)
    }
  }
}