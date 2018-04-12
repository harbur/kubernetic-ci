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
        helmCmd.init()
        helmCmd.pack()
      }

      stage ('Test') {
        helmCmd.test()
      }

      stage ('Push') {
        helmCmd.push()
      }
      bitBucketCmd.successful()
    } catch (e){
      bitBucketCmd.failed()
      throw e
    }
  }
}
