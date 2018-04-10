def call(body) {

  def dockerCmd = new io.harbur.DockerCmd()
  def gitCmd = new io.harbur.GitCmd()

  node ("jenkins-jenkins-slave"){
    try{
      dockerCmd.login("docker.k8s.harbur.io")
      gitCmd.checkout()
  
      stage ('Build') {
        bitbucketStatusNotify(buildState: 'INPROGRESS', buildKey: 'build', buildName: 'Build')
        try {
          sh "kc build -t ${BRANCH_NAME}"
          bitbucketStatusNotify(buildState: 'SUCCESSFUL', buildKey: 'build', buildName: 'Build')
        } catch(Exception e) {
          bitbucketStatusNotify(buildState: 'FAILED', buildKey: 'build', buildName: 'Build',
            buildDescription: 'Something went wrong with build!'
          )
        }
      }
  
      stage ('Push') {
        bitbucketStatusNotify(buildState: 'INPROGRESS', buildKey: 'push', buildName: 'Push')
        try {
          sh "kc push -t ${BRANCH_NAME}"
          bitbucketStatusNotify( buildState: 'SUCCESSFUL', buildKey: 'push', buildName: 'Push')
        } catch(Exception e) {
          bitbucketStatusNotify(buildState: 'FAILED', buildKey: 'push', buildName: 'Push',
            buildDescription: 'Something went wrong while pushing image(s) to the registry!'
          )
        }
      }
    } catch (e){
      throw e
    }
  }
}
