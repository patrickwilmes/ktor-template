package lucene

import domain.Entity
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.TextField

fun <T : Entity> T.toDocument(indexFieldMap: Map<String, String?>): Document {
    val doc = Document()
    indexFieldMap.entries.forEach {
        doc.add(TextField(it.key, it.value ?: "", Field.Store.YES))
    }
    doc.add(TextField("idString", id.toString(), Field.Store.YES))
    return doc
}
