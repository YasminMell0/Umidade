const express = require('express');
const bodyParser = require('body-parser');
const { collection, addDoc, getDocs } = require('firebase/firestore');
const db = require('./firebaseConfig');
const cors = require('cors');
const app = express();
app.use(cors());
app.use(bodyParser.json());
// Rota para adicionar dados
app.post('/add', async (req, res) => {
  const { temperature, humidity } = req.body;
  if (temperature == null || humidity == null) {
    return res.status(400).send("Todos os campos são obrigatórios.");
  }
  try {
    // Gera o timestamp no momento da inserção
    const dateTime = new Date().toISOString(); // Formato ISO (ex.: "2024-11-27T12:34:56.789Z")
    await addDoc(collection(db, 'sensorData'), {
      dateTime,
      temperature,
      humidity
    });
    res.status(201).send("Dados adicionados com sucesso.");
  } catch (error) {
    res.status(500).send("Erro ao adicionar dados: " + error.message);
  }
});
// Rota para ler dados
app.get('/data', async (req, res) => {
  try {
    const querySnapshot = await getDocs(collection(db, 'sensorData'));
    const data = querySnapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
    res.status(200).json(data);
  } catch (error) {
    res.status(500).send("Erro ao buscar dados: " + error.message);
  }
});
const PORT = process.env.PORT || 3031;
app.listen(PORT, () => console.log(`Servidor rodando na porta ${PORT}`));






