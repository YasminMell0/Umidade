// Importações específicas do Firebase Modular SDK
const { initializeApp } = require('firebase/app');
const { getFirestore } = require('firebase/firestore');
// Configuração do Firebase
const firebaseConfig = {
    apiKey: "AIzaSyCQrNJ-gBKcEbeomiTN3J5CLCd_MFaUS2k",
    authDomain: "projetoumidade-5d4b7.firebaseapp.com",
    projectId: "projetoumidade-5d4b7",
    storageBucket: "projetoumidade-5d4b7.firebasestorage.app",
    messagingSenderId: "135762822453",
    appId: "1:135762822453:web:9dcceb4c9e663ef064387c"
};
// Inicializa o app Firebase e o Firestore
const app = initializeApp(firebaseConfig);
const db = getFirestore(app);
module.exports = db;