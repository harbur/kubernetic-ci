def call(body) {
  node ("jenkins-jenkins-slave"){
    try{
      kc_image = docker.image("docker.k8s.harbur.io/kc:1bff56a")

      stage ('Checkout') {
        checkout scm
      }

      stage ('Build') {
        kc_image.inside  {
            sh "kc build"
        }
      }

      stage ('Push') {
        kc_image.inside  {
            sh "kc push"
        }
      }

    } catch (e){
      throw e
    }
  }
}
