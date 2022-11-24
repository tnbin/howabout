package com.example.howabout.Search;

import java.util.List;

public class CategoryResult {
//    @SerializedName("documents")
//    @Expose
    private List<Document> documents;

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return "CategoryResult{" +
                "documents=" + documents.get(0).toString() +
                '}';
    }
}
