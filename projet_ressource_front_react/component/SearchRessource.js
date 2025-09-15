import { StyleSheet, Text, View, FlatList, Image, useWindowDimensions, TouchableOpacity, TextInput, SafeAreaView, KeyboardAvoidingView, ScrollView, Platform } from 'react-native';
import React, { useEffect, useState } from 'react';
import { Picker } from '@react-native-picker/picker';
import { useFocusEffect } from '@react-navigation/native';
import Header from '../component/Header';
import globalStyles from '../Styles/globalStyles';
import { getApiUrl } from '../apiConfig';

const SearchRessource = ({ navigation }) => {
  const [text, setText] = useState("");
  const [filtreRessources, setFiltreRessources] = useState([]);
  const [ressources, setRessources] = useState([]);
  const [type_relation, setType_Relation] = useState([]);
  const [selectedTypeRelation, setSelectedTypeRelation] = useState(null);
  const [categorie_ressource, setCategorie_Ressource] = useState([]);
  const [selectedCategorieRessource, setSelectedCategorieRessource] = useState(null);
  const [type_ressource, setType_Ressource] = useState([]);
  const [selectedTypeRessource, setSelectedTypeRessource] = useState(null);
  const window = useWindowDimensions();

  // Définir la taille d'un bloc (carte ressource)
  const ITEM_WIDTH = 300; // en px
  const SPACING = ITEM_WIDTH / 10;
  // Calcul dynamique du nombre de colonnes selon la taille de l'écran
  const numColumns = Math.max(1, Math.floor(window.width / (ITEM_WIDTH + SPACING)));

  //-- Liste les ressources
  const fetchRessources = async () => {
    try {
      const response = await fetch(`${getApiUrl()}/ressources`);
      console.log(response);
      if (!response.ok) throw new Error("Réponse réseau non OK");

      const data = await response.json();

      if (Array.isArray(data)) {
        setRessources(data);
        setFiltreRessources(data);
      } else {
        console.error("Données inattendues");
      }
    } catch (error) {
      console.error("Erreur de fetch", error);
    }
  };

  //-- Rafraîchit les données à chaque fois que l'écran est affiché
  useFocusEffect(
    React.useCallback(() => {
      fetchRessources();
    }, [])
  );

  //-- Applique les filtres
  useEffect(() => {
    let result = ressources;

    //-- Filtre sur le texte du titre
    if (text.length > 2) {
      result = result.filter((ressource) =>
        ressource.titre.toLowerCase().includes(text.toLowerCase())
      );
    }

    //-- Filtre sur le type de relation
    if (selectedTypeRelation) {
      result = result.filter((ressource) =>
        ressource.type_relation.some((type) => type.id === parseInt(selectedTypeRelation))
      );
    }

    //-- Filtre sur la catégorie de ressource
    if (selectedCategorieRessource) {
      result = result.filter((ressource) =>
        ressource.categorie_ressource.id === parseInt(selectedCategorieRessource)
      );
    }

    //-- Filtre sur le type de ressource
    if (selectedTypeRessource) {
      result = result.filter((ressource) =>
        ressource.type_ressource.id === parseInt(selectedTypeRessource)
      );
    }

    setFiltreRessources(result);
  }, [text, ressources, selectedTypeRelation, selectedCategorieRessource, selectedTypeRessource]);

  //-- Récupère la liste des type_relation
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

  //-- Récupère la liste des catégories de ressource
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

  //-- Récupère la liste des types de ressource
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

  const dataToDisplay = filtreRessources;

  //-- Affichage
  return (
    <SafeAreaView style={globalStyles.container}>
      <KeyboardAvoidingView
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        style={globalStyles.keyboardAvoidingView}
      >
        {/* Entête */}
        <View>
          <Header />
        </View>

        <View style={globalStyles.container}>
          {/* Combobox type de relation */}
          <View style={globalStyles.standardInputContainer}>
            <Picker
              selectedValue={selectedTypeRelation}
              style={globalStyles.standardInput}
              onValueChange={(itemValue) => setSelectedTypeRelation(itemValue)}
            >
              <Picker.Item label="Tous les types de relation" value="" />
              {type_relation.map((type) => (
                <Picker.Item
                  key={type.id}
                  label={type.libelle ?? "Type inconnu"}
                  value={type.id}
                />
              ))}
            </Picker>
          </View>

          {/* Combobox catégorie de ressource */}
          <View style={globalStyles.standardInputContainer}>
            <Picker
              selectedValue={selectedCategorieRessource}
              style={globalStyles.standardInput}
              onValueChange={(itemValue) => setSelectedCategorieRessource(itemValue)}
            >
              <Picker.Item label="Toutes les catégories" value="" />
              {categorie_ressource.map((type) => (
                <Picker.Item
                  key={type.id}
                  label={type.libelle ?? "Catégorie inconnue"}
                  value={type.id}
                />
              ))}
            </Picker>
          </View>

          {/* Combobox type de ressource */}
          <View style={globalStyles.standardInputContainer}>
            <Picker
              selectedValue={selectedTypeRessource}
              style={globalStyles.standardInput}
              onValueChange={(itemValue) => setSelectedTypeRessource(itemValue)}
            >
              <Picker.Item label="Tous les types de ressource" value="" />
              {type_ressource.map((type) => (
                <Picker.Item
                  key={type.id}
                  label={type.libelle ?? "Type inconnu"}
                  value={type.id}
                />
              ))}
            </Picker>
          </View>

          {/* Barre de recherche textuelle */}
          <View style={globalStyles.standardInputContainer}>
            <TextInput
              style={globalStyles.standardInput}
              placeholder="Rechercher une ressource"
              onChangeText={setText}
              value={text}
            />
          </View>

          {/* Liste des ressources */}
          <FlatList
            data={dataToDisplay}
            numColumns={numColumns}
            columnWrapperStyle={
              numColumns > 1 ? globalStyles.columnWrapper : null
            }
            contentContainerStyle={globalStyles.listContent}
            keyExtractor={(item) => item.id.toString()}
            renderItem={({ item }) => (
              <View style={[globalStyles.itemContainer, { width: ITEM_WIDTH }]}>
                {/* Cliquable */}
                <TouchableOpacity
                  onPress={() =>
                    navigation.navigate("Ma ressource", { ressourceId: item.id })
                  }
                >
                  {/* Image */}
                  <Image
                    source={{ uri: "https://cdn-icons-png.flaticon.com/32/716/716784.png" }}
                    style={globalStyles.image}
                  />
                  {/* Titre */}
                  <Text style={globalStyles.itemTitleText}>
                    {item?.titre ?? "Nom inconnu"}
                  </Text>
                  {/* Types de relation */}
                  <Text style={globalStyles.itemText}>
                    Relation : {item.type_relation?.map(tr => tr.libelle).join(", ")}
                  </Text>
                  {/* Catégorie de ressource */}
                  <Text style={globalStyles.itemText}>
                    Catégorie : {item.categorie_ressource?.libelle ?? "Catégorie inconnue"}
                  </Text>
                  {/* Type de ressource */}
                  <Text style={globalStyles.itemText}>
                    Type : {item.type_ressource?.libelle ?? "Type inconnu"}
                  </Text>
                </TouchableOpacity>
              </View>
            )}
          />
        </View>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
};

export default SearchRessource;
