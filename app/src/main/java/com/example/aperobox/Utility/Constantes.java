package com.example.aperobox.Utility;

public class Constantes {
    public static String URL_API = "https://aperoboxapi.azurewebsites.net/api/";
    public static String URL_IMAGE_API = "https://res.cloudinary.com/aperobox/image/upload/v1575920447/aperobox/box/";
    public static String URL_IMAGE_CIRLCE = "w_1000,c_fill,ar_1:1,g_auto,r_max,bo_5px_solid_red,b_rgb:262c35/";
    public static String DEFAULT_END_URL_IMAGE_API = "box_complete.jpg";


    public static String HTTP_STATUS_400_BAD_REQUEST = "La syntaxe de la requête est erronée.";
    public static String HTTP_STATUS_401_UNAUTHORIZED = "Une authentification est nécessaire pour accéder à la ressource.";
    public static String HTTP_STATUS_403_FORBIDDEN = "Vous ne disposez pas de droits suffisent pour accéder à cette/ces ressource(s)";
    public static String HTTP_STATUS_404_NOT_FOUND = "La page que vous recherchez est introuvable.";
    public static String HTTP_STATUS_405_METHOD_NOT_ALLOWED = "Requète non autorisée.";
    public static String HTTP_STATUS_408_REQUEST_TIME_OUT = "La demande n'est pas parvenue dans les délais demandé au serveur.\nVeuillez réessayer plus tard.";

    public static String HTTP_STATUS_500_INTERNAL_SERVER_ERROR = "Une erreur est survenue sur notre serveur. Veuillez réessayer plus tard.\nSi le problème persiste, n'hésitez pas à nous contacter par mail via le menu.";



}
