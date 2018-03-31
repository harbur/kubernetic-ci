def call(body) {
  node ("jenkins-jenkins-slave"){
    try{
      kc_image = docker.image("docker.k8s.harbur.io/kc:04d0fc5")
      kc_image.inside  {

        stage ('Checkout') {
          checkout scm
        }

        stage ('Build') {
          sh "printenv"
          sh "kc build -t ${BRANCH_NAME}"
        }

        stage ('Push') {
          sh "kc push -t ${BRANCH_NAME}"
        }
      }
    } catch (e){
      throw e
    }
  }
}
