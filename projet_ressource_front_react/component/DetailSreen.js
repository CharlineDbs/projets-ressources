import { Text, View, Image, TouchableOpacity, Alert, Platform, Linking } from 'react-native';
import { useState, useEffect } from 'react';
import { SafeAreaView } from 'react-native';
import Header from '../component/Header';
import globalStyles from '../Styles/globalStyles';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { getApiUrl } from '../apiConfig';


export default function DetailsScreen({ navigation, route }) {
  const [ressource, setRessource] = useState(null);
  const [file, setFile] = useState("");
  const [userId, setUserId] = useState(null);

  //-- Récupère l'id de l'utilisateur connecté
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

  //-- Récupère les infos de la ressource
  const fetchRessource = (id) => {
    fetch(`${getApiUrl()}/ressources/${id}`)
      .then((res) => {
        console.log("Status de la réponse:", res.status);
        if (!res.ok) throw new Error("Réponse réseau non OK");
        return res.json();
      })
      .then((data) => setRessource(data))
      .catch((err) => console.error("Erreur de fetch :", err));
  };

  //-- Charge les infos de la ressource
  useEffect(() => {
    const { ressourceId } = route.params;
    fetchRessource(ressourceId);
  }, [route.params]);

  //-- Met à jour les infos si modifications
  useEffect(() => {
    if (route.params?.refresh) {
      fetchRessource(route.params.ressourceId);
    }
  }, [route.params?.refresh]);

  //-- Fonction de suppression de la ressource
  const deleteRessource = async () => {
    const isConfirmed = await confirmation();
    if (isConfirmed && ressource) {
      try {
        const response = await fetch(`${getApiUrl()}/ressources/${ressource.id}`, {
          method: 'DELETE',
        });

        if (response.status === 204) {
          navigation.navigate("Ressources");
        } else {
          console.error("Échec de la suppression");
        }
      } catch (error) {
        console.error("Erreur lors de la suppression :", error);
      }
    }
  };

  //-- Demande la confirmation avant suppression
  const confirmation = () => {
    return new Promise((resolve) => {
      if (Platform.OS === 'web') {
        const confirmed = window.confirm("Voulez-vous vraiment supprimer cette ressource ?");
        resolve(confirmed);
      } else {
        Alert.alert(
          "Confirmation",
          "Voulez-vous vraiment supprimer cette ressource ?",
          [
            {
              text: "Annuler",
              style: "cancel",
              onPress: () => resolve(false),
            },
            {
              text: "Supprimer",
              style: "destructive",
              onPress: () => resolve(true),
            },
          ],
          { cancelable: true }
        );
      }
    });
  };

  //-- Contrôle la présence d'un fichier uploadé
  useEffect(() => {
    const fileExist = async () => {
      try {
        const { ressourceId } = route.params;
        const response = await fetch(`${getApiUrl()}/ressources/${ressourceId}/fileExist`);
        if (!response.ok) throw new Error("Réponse réseau non OK");

        const data = await response.text();
        setFile(data);
      } catch (error) {
        console.error("FileExist : Erreur de fetch", error);
      }
    };
    fileExist();
  }, [route.params]);

  //-- Téléchargement du fichier de la ressource
  const handleDownload = () => {
    const downloadUrl = `${getApiUrl()}/ressources/${ressource.id}/download`;
    if (Platform.OS === 'web') {
      window.open(downloadUrl, '_blank');
    } else {
      Linking.openURL(downloadUrl).catch(err => console.error("Erreur lors de l'ouverture du lien :", err));
    }
  };

  //-- Format de date : 15/05/2025 15:19
  const formatDate = (isoDate) => {
    const date = new Date(isoDate);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${day}/${month}/${year} ${hours}:${minutes}`;
  };

  //-- Affiche "Loading" si les infos ne sont pas récupérées
  if (!ressource) {
    return <Text>Loading...</Text>;
  }

  //-- Affichage
  return (
    <SafeAreaView style={globalStyles.container}>
      <Header />
      <View style={globalStyles.container}>
        {/* Fiche de la ressource */}
        <Image
          source={{ uri: "https://cdn-icons-png.flaticon.com/32/716/716784.png" }}
          style={globalStyles.image}
        />
        <Text style={globalStyles.ressourceTitle}>{ressource.titre}</Text>
        <Text style={globalStyles.ressourceText}>
          Créé le : {formatDate(ressource.dateCreation)} par {ressource.utilisateur?.pseudo}
        </Text>
        <Text style={globalStyles.ressourceText}>
          Relation : {ressource.type_relation?.map(tr => tr.libelle).join(", ")}
        </Text>
        <Text style={globalStyles.ressourceText}>
          Catégorie : {ressource.categorie_ressource?.libelle}
        </Text>
        <Text style={globalStyles.ressourceText}>
          Type : {ressource.type_ressource?.libelle}
        </Text>
        <Text style={globalStyles.ressourceText}>{ressource.contenu}</Text>

        {/* Bouton de téléchargement : seulement si un fichier est présent */}
        {file && (
          <TouchableOpacity style={globalStyles.standardButton} onPress={handleDownload}>
            <Text style={globalStyles.standardButtonText}>Télécharger le fichier</Text>
          </TouchableOpacity>
        )}
        {file && <Text style={globalStyles.ressourceText}>Fichier : {file}</Text>}

        {/* Bouton de modification : seulement pour l'auteur de la ressource */}
        {userId == ressource.utilisateur?.id && (
          <TouchableOpacity
            style={globalStyles.standardButton}
            onPress={() => navigation.navigate('Edition', { ressourceId: ressource.id })}
          >
            <Text style={globalStyles.standardButtonText}>Modifier</Text>
          </TouchableOpacity>
        )}

        {/* Bouton de suppression : seulement pour l'auteur de la ressource */}
        {userId == ressource.utilisateur?.id && (
          <TouchableOpacity
            style={globalStyles.standardButton}
            onPress={deleteRessource}
          >
            <Text style={globalStyles.standardButtonText}>Supprimer</Text>
          </TouchableOpacity>
        )}
      </View>
    </SafeAreaView>
  );
}
