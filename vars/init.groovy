def call(body) {

  def dockerCmd = new io.harbur.DockerCmd()
  def gitCmd = new io.harbur.GitCmd()
  def helmCmd = new io.harbur.HelmCmd()
  def bitBucketCmd = new io.harbur.BitBucketCmd()

  node ("jenkins-jenkins-slave"){
    try{
      bitBucketCmd.inProgress()
      dockerCmd.login("docker.k8s.harbur.io")
      gitCmd.checkout()

      stage ('Build') {
        sh "kc build -t ${BRANCH_NAME}"
      }

      stage ('Push') {
        sh "kc push -t ${BRANCH_NAME}"
      }

      bitBucketCmd.successful()
    } catch (e){
      bitBucketCmd.failed()
      throw e
    }
  }
}
