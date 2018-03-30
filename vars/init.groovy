def call(body) {
  node ("jenkins-jenkins-slave"){
    try{
      kc_image = docker.image("docker.k8s.harbur.io/kc:aa45cbb")
      kc_image.inside  {

        stage ('Checkout') {
          checkout scm
        }

        stage ('Build') {
          sh "printenv"
          sh "kc build"
        }

        stage ('Push') {
          sh "kc push"
        }
      }
    } catch (e){
      throw e
    }
  }
}
