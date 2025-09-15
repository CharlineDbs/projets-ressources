import { StyleSheet, Text, View, Image, TouchableOpacity, TextInput, Dimensions, Platform } from 'react-native';
import { useState, useEffect } from 'react';
import { SafeAreaView } from 'react-native';
import { Picker } from '@react-native-picker/picker';
import MultiSelect from 'react-native-multiple-select';
import Header from '../component/Header';
import globalStyles from '../Styles/globalStyles';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { getApiUrl } from '../apiConfig';

const screenWidth = Dimensions.get('window').width;

export default function EditScreen({ navigation, route }) {
  const isEditMode = !!route.params?.ressourceId;
  const [ressource, setRessource] = useState(null);
  const [categorie_ressource, setCategorie_Ressource] = useState([]);
  const [selectedCategorieRessource, setSelectedCategorieRessource] = useState(null);
  const [type_ressource, setType_Ressource] = useState([]);
  const [selectedTypeRessource, setSelectedTypeRessource] = useState(null);
  const [type_relation, setType_Relation] = useState([]);
  const [selectedTypeRelation, setSelectedTypeRelation] = useState([]);
  const [titre, setTitre] = useState("");
  const [contenu, setContenu] = useState("");
  const [selectedFile, setSelectedFile] = useState(null);
  const [userId, setUserId] = useState(null);

  // Valeur de ressource par défaut
  const defaultRessource = {
    id: null,
    titre: '',
    contenu: '',
    dateCreation: new Date().toISOString(),
    valide: true,
    suspendu: false,
    utilisateur: userId,
    categorie_ressource: null,
    type_ressource: null,
    type_relation: [],
  };

  // Récupère l'id de l'utilisateur connecté
  useEffect(() => {
    const fetchUser = async () => {
      try {
        const id = await AsyncStorage.getItem('userId');
        setUserId(id);
      } catch (err) {
        console.error('Erreur chargement user id:', err);
      }
    };
    fetchUser();
  }, []);

  // Récupère les infos de la ressource ou ressource par défaut si création
  useEffect(() => {
    if (isEditMode) {
      const { ressourceId } = route.params;
      fetch(`${getApiUrl()}/ressources/${ressourceId}`)
        .then((res) => {
          if (!res.ok) throw new Error("Réponse réseau non OK");
          return res.json();
        })
        .then((data) => {
          setRessource(data);
          setTitre(data.titre || "");
          setSelectedCategorieRessource(data.categorie_ressource?.id || null);
          setSelectedTypeRessource(data.type_ressource?.id || null);
          setSelectedTypeRelation(data.type_relation?.map((tr) => tr.id) || []);
          setContenu(data.contenu || "");
        })
        .catch((err) => console.error("Erreur de fetch :", err));
    } else {
      setRessource(defaultRessource);
      setTitre(defaultRessource.titre);
      setContenu(defaultRessource.contenu);
      setSelectedCategorieRessource(defaultRessource.categorie_ressource);
      setSelectedTypeRessource(defaultRessource.type_ressource);
      setSelectedTypeRelation(defaultRessource.type_relation);
    }
  }, [route.params, userId]);

  // Récupère la liste des catégories de ressource
  useEffect(() => {
    const fetchCategorie_Ressource = async () => {
      try {
        const response = await fetch(`${getApiUrl()}/categorie_ressource`);
        if (!response.ok) throw new Error("Réponse réseau non OK");
        const data = await response.json();
        if (Array.isArray(data)) {
          setCategorie_Ressource(data);
        } else {
          console.error("Données inattendues");
        }
      } catch (error) {
        console.error("Erreur de fetch", error);
      }
    };
    fetchCategorie_Ressource();
  }, []);

  // Récupère la liste des types de ressource
  useEffect(() => {
    const fetchType_Ressource = async () => {
      try {
        const response = await fetch(`${getApiUrl()}/type_ressource`);
        if (!response.ok) throw new Error("Réponse réseau non OK");
        const data = await response.json();
        if (Array.isArray(data)) {
          setType_Ressource(data);
        } else {
          console.error("Données inattendues");
        }
      } catch (error) {
        console.error("Erreur de fetch", error);
      }
    };
    fetchType_Ressource();
  }, []);

  // Récupère la liste des type_relation
  useEffect(() => {
    const fetchType_Relation = async () => {
      try {
        const response = await fetch(`${getApiUrl()}/type_relation`);
        if (!response.ok) throw new Error("Réponse réseau non OK");
        const data = await response.json();
        if (Array.isArray(data)) {
          setType_Relation(data);
        } else {
          console.error("Données inattendues");
        }
      } catch (error) {
        console.error("Erreur de fetch", error);
      }
    };
    fetchType_Relation();
  }, []);

  // Sélection d'un fichier (uniquement pour le web)
  const handleFileChange = (event) => {
    if (Platform.OS === 'web') {
      const file = event.target.files[0];
      if (file) {
        setSelectedFile(file);
      }
    } else {
      alert("La sélection de fichiers n'est disponible que sur la version web.");
    }
  };

  // Fonction pour enregistrer
  const handleSave = async () => {
    const method = isEditMode ? 'PUT' : 'POST';
    const url = isEditMode
      ? `${getApiUrl()}/ressources/${ressource.id}`
      : `${getApiUrl()}/ressources`;
    const userIdRessource = isEditMode ? ressource.utilisateur.id : userId;
    const ressourceModifiee = {
      id: isEditMode ? ressource.id : undefined,
      titre: titre,
      contenu: contenu,
      dateCreation: ressource.dateCreation,
      valide: ressource.valide,
      suspendu: ressource.suspendu,
      utilisateur: { id: userIdRessource },
      categorie_ressource: { id: selectedCategorieRessource },
      type_ressource: { id: selectedTypeRessource },
      type_relation: selectedTypeRelation.map(id => ({ id })),
    };

    try {
      const response = await fetch(url, {
        method: method,
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(ressourceModifiee),
      });

      if (!response.ok) {
        throw new Error("Échec de la mise à jour de la ressource.");
      }

      if (isEditMode) {
        if (selectedFile != null) {
          await uploadFile(ressourceModifiee.id, selectedFile);
        }
        navigation.replace("Ma ressource", { ressourceId: ressourceModifiee.id, refresh: true });
      } else {
        const newRessource = await response.json();
        if (selectedFile != null) {
          await uploadFile(newRessource.id, selectedFile);
        }
        navigation.replace("Ma ressource", { ressourceId: newRessource.id });
      }
    } catch (error) {
      console.error("Erreur lors de la mise à jour :", error);
      alert("Erreur lors de la mise à jour.");
    }
  };

  // Envoi du fichier vers l'API
  const uploadFile = async (ressourceId, file) => {
    const url = `${getApiUrl()}/ressources/${ressourceId}/upload`;
    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch(url, {
        method: 'POST',
        body: formData,
      });

      if (!response.ok) {
        throw new Error('Erreur lors de l’upload');
      }

      const result = await response.text();
      console.log('Réponse serveur :', result);
    } catch (error) {
      console.error('Erreur upload :', error);
      alert('Erreur upload : ' + error.message);
    }
  };

  return (
    <SafeAreaView style={globalStyles.container}>
      <View>
        <Header />
      </View>
      <View style={globalStyles.container}>
        <Image
          source={{ uri: "https://cdn-icons-png.flaticon.com/32/716/716784.png" }}
          style={globalStyles.image}
        />
        <View style={globalStyles.standardInputContainer}>
          <TextInput
            style={globalStyles.standardInput}
            placeholder="Titre"
            onChangeText={setTitre}
            value={titre}
          />
        </View>
        <View style={globalStyles.standardInputContainer}>
          <MultiSelect
            style={globalStyles.standardInput}
            items={type_relation}
            uniqueKey="id"
            onSelectedItemsChange={setSelectedTypeRelation}
            selectedItems={selectedTypeRelation}
            selectText="Relation(s)"
            searchInputPlaceholderText="Rechercher..."
            tagRemoveIconColor="#CCC"
            tagBorderColor="#CCC"
            tagTextColor="#000"
            selectedItemTextColor="#007AFF"
            selectedItemIconColor="#007AFF"
            itemTextColor="#000"
            displayKey="libelle"
            searchInputStyle={{ color: '#CCC' }}
            submitButtonColor="#007AFF"
            submitButtonText="Valider"
          />
        </View>
        <View style={globalStyles.standardInputContainer}>
          <Picker
            selectedValue={selectedCategorieRessource}
            style={globalStyles.standardInput}
            onValueChange={(itemValue) => setSelectedCategorieRessource(itemValue)}
          >
            {categorie_ressource.map((type) => (
              <Picker.Item
                key={type.id}
                label={type.libelle ?? "Catégorie inconnue"}
                value={type.id}
              />
            ))}
          </Picker>
        </View>
        <View style={globalStyles.standardInputContainer}>
          <Picker
            selectedValue={selectedTypeRessource}
            style={globalStyles.standardInput}
            onValueChange={(itemValue) => setSelectedTypeRessource(itemValue)}
          >
            {type_ressource.map((type) => (
              <Picker.Item
                key={type.id}
                label={type.libelle ?? "Type inconnu"}
                value={type.id}
              />
            ))}
          </Picker>
        </View>
        <View style={globalStyles.standardInputContainer}>
          <TextInput
            style={globalStyles.standardInput}
            placeholder="Contenu"
            onChangeText={setContenu}
            value={contenu}
          />
        </View>
        {Platform.OS === 'web' && (
          <input type="file" onChange={handleFileChange} style={{ margin: 10 }} />
        )}
        {selectedFile && <Text>Fichier sélectionné : {selectedFile.name}</Text>}
        <TouchableOpacity style={globalStyles.standardButton} onPress={handleSave}>
          <Text style={globalStyles.standardButtonText}>Enregistrer</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
}
