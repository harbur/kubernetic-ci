def call(body) {
  node ("jenkins-jenkins-slave"){
    try{
      stage 'Init'
      echo "hello"
      stage 'Build'
      echo "done"
    } catch (e){
      throw e
    }
  }
}
