package com.example.howabout.Vo;

import com.example.howabout.CategoryResult.Document;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult {

    public SearchResult(List<Document> documents) {
        this.documents = documents;
    }

    @SerializedName("documents")
    @Expose
    List<Document> documents=null;

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "documents=" + documents +
                '}';
    }
}
