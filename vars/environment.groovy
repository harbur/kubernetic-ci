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

      stage ('Deploy') {
        helmCmd.init()
        sh "helm dep build env/sandbox/"
        sh "helm upgrade -i sandbox env/sandbox/ --namespace kc-staging"
      }

      bitBucketCmd.successful()
    } catch (e){
      bitBucketCmd.failed()
    }
  }
}
