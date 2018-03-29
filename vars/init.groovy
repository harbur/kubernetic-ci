def call(body) {
  node ("jenkins-jenkins-slave"){
    try{
      stage 'Init'
      sh "docker build -t demo ."
      echo "hello"
      stage 'Build'
      echo "done"
    } catch (e){
      throw e
    }
  }
}
