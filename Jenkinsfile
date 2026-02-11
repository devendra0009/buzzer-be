pipeline {
    agent any

    environment {
        AWS_REGION = "us-east-1"
        ECR_REPO = "780886633405.dkr.ecr.us-east-1.amazonaws.com/buzzerbackend"
        IMAGE_TAG = "latest"
        EC2_HOST = "3.80.215.101"
    }

    stages {

        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
//                     sh "docker build -t buzzerbackend:${IMAGE_TAG} ."
                    bat "docker build -t buzzerbackend:%IMAGE_TAG% ."
                }
            }
        }

//         stage('Login to ECR') {
//             steps {
//                 withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-creds']]) {
//                     sh """
//                     aws ecr get-login-password --region ${AWS_REGION} | \
//                     docker login --username AWS --password-stdin ${ECR_REPO}
//                     """
//                 }
//             }
//         }

        stage('Login to ECR') {
            steps {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-creds']]) {
                    bat """
                    aws ecr get-login-password --region %AWS_REGION% | docker login --username AWS --password-stdin %ECR_REPO%
                    """
                }
            }
        }


        stage('Tag & Push Image') {
            steps {
//                 sh """
//                 docker tag buzzerbackend:${IMAGE_TAG} ${ECR_REPO}:${IMAGE_TAG}
//                 docker push ${ECR_REPO}:${IMAGE_TAG}
//                 """

            bat """
            docker tag buzzerbackend:%IMAGE_TAG% %ECR_REPO%:%IMAGE_TAG%
            docker push %ECR_REPO%:%IMAGE_TAG%
            """

            }
        }

        stage('Deploy to EC2') {
            steps {
//                 sshagent(['ec2-ssh']) {
//                     sh """
//                     ssh -o StrictHostKeyChecking=no ec2-user@${EC2_HOST} '
//
//                     aws ecr get-login-password --region ${AWS_REGION} | \
//                     docker login --username AWS --password-stdin ${ECR_REPO} &&
//
//                     docker pull ${ECR_REPO}:${IMAGE_TAG} &&
//
//                     docker stop buzzer-backend || true &&
//                     docker rm buzzer-backend || true &&
//
//                     docker run -d \
//                         --name buzzer-backend \
//                         -p 80:8080 \
//                         ${ECR_REPO}:${IMAGE_TAG}
//                     '
//                     """

//                  bat """
//                             ssh -o StrictHostKeyChecking=no ec2-user@%EC2_HOST% ^
//                             "aws ecr get-login-password --region %AWS_REGION% | docker login --username AWS --password-stdin %ECR_REPO% && ^
//                             docker pull %ECR_REPO%:%IMAGE_TAG% && ^
//                             docker stop buzzer-backend || exit 0 && ^
//                             docker rm buzzer-backend || exit 0 && ^
//                             docker run -d --name buzzer-backend -p 80:8080 %ECR_REPO%:%IMAGE_TAG%"
//                             """
//                 }

// give keys path wherever stored in windows as jenkins is running on windows .
//      bat """
//             ssh -i C:\\keys\\buzzer-kp-11feb.pem -o StrictHostKeyChecking=no ec2-user@%EC2_HOST% ^
//             "aws ecr get-login-password --region %AWS_REGION% | docker login --username AWS --password-stdin %ECR_REPO% && ^
//             docker pull %ECR_REPO%:%IMAGE_TAG% && ^
//             docker stop buzzer-backend || true && ^
//             docker rm buzzer-backend || true && ^
//             docker run -d --name buzzer-backend -p 80:8080 %ECR_REPO%:%IMAGE_TAG%"
//             """


// withCredentials([sshUserPrivateKey(
//     credentialsId: 'ec2-ssh',
//     keyFileVariable: 'SSH_KEY'
// )]) {
//
//     bat """
//     ssh -i %SSH_KEY% -o StrictHostKeyChecking=no ec2-user@3.80.215.101 ^
//     "docker pull 780886633405.dkr.ecr.us-east-1.amazonaws.com/buzzerbackend:latest && ^
//     docker stop buzzer-backend || exit 0 && ^
//     docker rm buzzer-backend || exit 0 && ^
//     docker run -d --name buzzer-backend -p 80:8080 780886633405.dkr.ecr.us-east-1.amazonaws.com/buzzerbackend:latest"
//     """
// }

sshagent(['ec2-ssh']) {
    bat """
    ssh -o StrictHostKeyChecking=no ec2-user@3.80.215.101 ^
    "docker pull 780886633405.dkr.ecr.us-east-1.amazonaws.com/buzzerbackend:latest && ^
    docker stop buzzer-backend || exit 0 && ^
    docker rm buzzer-backend || exit 0 && ^
    docker run -d --name buzzer-backend -p 80:8080 780886633405.dkr.ecr.us-east-1.amazonaws.com/buzzerbackend:latest"
    """
}


            }
        }
    }

    post {
        success {
            echo "Deployment Successful 🚀"
        }
        failure {
            echo "Deployment Failed ❌"
        }
    }
}
