package io.harbur.cmds

/**
 * Class for Git commands.
 */
class Git {

  /**
   * Clones the Git repository and exposes SCM variables as environment variables:
   *
   * - `GIT_COMMIT`: commit ID
   * - `GIT_COMMIT_SHORT`: Short commit ID (the first 7 characters)
   * - `GIT_BRANCH`: branch name
   * - `GIT_PREVIOUS_COMMIT`: previous commit ID
   * - `GIT_PREVIOUS_SUCCESSFUL_COMMIT`: previous successful commit ID
   * - `GIT_URL`: git URL
   *
   * @param script Reference to script scope to access `env` and `checkout()`
   */
  static def checkout(def script) {
    def env = script.env

    def scmVars = script.checkout(script.scm)

    // Make SCM variables available in environment
    env.GIT_COMMIT=scmVars.GIT_COMMIT
    env.GIT_COMMIT_SHORT=scmVars.GIT_COMMIT[0..6]
    env.GIT_BRANCH=scmVars.GIT_BRANCH
    env.GIT_PREVIOUS_COMMIT=scmVars.GIT_PREVIOUS_COMMIT
    env.GIT_PREVIOUS_SUCCESSFUL_COMMIT=scmVars.GIT_PREVIOUS_SUCCESSFUL_COMMIT
    env.GIT_URL=scmVars.GIT_URL

    // Get project name
    env.PROJECT_NAME = env.JOB_NAME.split("/")[-1]
  }
}
