package com.example.aperobox.Dao;

import android.media.Image;

import com.example.aperobox.Dao.JsonTranslator.ImageJsonTranslator;
import com.example.aperobox.Utility.Constantes;

public class ImageDAO {

    public ImageDAO(){}

    public Image getImageFromUrl(String url) throws Exception{
        return ImageJsonTranslator.jsonToLigneProduit(UtilDAO.getSimpleLine(Constantes.URL_IMAGE_API));
    }
}
