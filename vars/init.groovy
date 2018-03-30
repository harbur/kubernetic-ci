def call(body) {
  node ("jenkins-jenkins-slave"){
    try{
      stage ('Build') {
        checkout scm

        kc_image = docker.image("docker.k8s.harbur.io/kc:1bff56a")
        kc_image.inside  {
            sh "echo hello"
        }
        
        //withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'KUBERNETIC_ACCESS_KEY', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
          //sh "echo $USERNAME - $PASSWORD ::::"
        //}

        sh "docker build -t demo ."
        echo "hello"
      }
      stage ('Push') {
        echo "done"
      }
    } catch (e){
      throw e
    }
  }
}
