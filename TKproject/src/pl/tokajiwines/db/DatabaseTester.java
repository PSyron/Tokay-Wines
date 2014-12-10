
package pl.tokajiwines.db;

import android.content.Context;

import pl.tokajiwines.jsonresponses.WineListItem;
import pl.tokajiwines.models.Address;
import pl.tokajiwines.models.Currency;
import pl.tokajiwines.models.Description;
import pl.tokajiwines.models.Flavour;
import pl.tokajiwines.models.Grade;
import pl.tokajiwines.models.Image;
import pl.tokajiwines.models.News;
import pl.tokajiwines.models.Producer;
import pl.tokajiwines.models.ProducerImage;
import pl.tokajiwines.models.Wine;
import pl.tokajiwines.models.WineStrain;
import pl.tokajiwines.utils.Log;

import java.util.List;

public class DatabaseTester {

    ProducersDataSource pDs;
    ProducerImagesDataSource piDs;
    AddressesDataSource aDs;
    CurrenciesDataSource cDs;
    DescriptionsDataSource dDs;
    FlavoursDataSource fDs;
    GradesDataSource gDs;
    ImagesDataSource iDs;
    NewsDataSource nDs;
    WinesDataSource wDs;
    WineStrainsDataSource wsDs;

    public DatabaseTester(Context context) {
        pDs = new ProducersDataSource(context);
        piDs = new ProducerImagesDataSource(context);
        aDs = new AddressesDataSource(context);
        cDs = new CurrenciesDataSource(context);
        dDs = new DescriptionsDataSource(context);
        fDs = new FlavoursDataSource(context);
        gDs = new GradesDataSource(context);
        iDs = new ImagesDataSource(context);
        nDs = new NewsDataSource(context);
        wDs = new WinesDataSource(context);
        wsDs = new WineStrainsDataSource(context);

    };

    public void testProducers() {
        pDs.open();

        pDs.insertProducer(new Producer(1, "Email", "www.test.pl", "Testowy", "666666666", 1, 1, 1,
                1, 1, "Dawno"));
        pDs.insertProducer(new Producer(2, "Email", "www.test.pl", "Testowy", "666666666", 1, 1, 1,
                1, 1, "Dawno"));
        List<Producer> prodlist = null;
        pDs.close();
        Log.i("Producent1", prodlist.get(0).mName + " " + prodlist.get(0).mIdDescription_ + " "
                + prodlist.get(0).mIdImageCover_ + " " + prodlist.get(0).mIdProducer);
        Log.i("Producent2", prodlist.get(1).mName + " " + prodlist.get(1).mIdDescription_ + " "
                + prodlist.get(1).mIdImageCover_ + " " + prodlist.get(1).mIdProducer);

    }

    public void testProducerImages() {
        piDs.open();
        piDs.insertImage(new ProducerImage(1, 1, 1, "pierwszy"));
        piDs.insertImage(new ProducerImage(2, 1, 1, "drugi"));
        List<ProducerImage> pIL = piDs.getAllImages();
        piDs.close();
        Log.i("ProducerImages1", "Image id: " + pIL.get(0).mIdImage_);
        Log.i("ProducerImages2", "Image id: " + pIL.get(1).mIdImage_);

    }

    public void testAddresses() {
        aDs.open();
        aDs.insertAddress(new Address(1, "Wroclawska", 26, "KWR", "55-002", 2.00, 3.00, "Dawno"));
        aDs.insertAddress(new Address(2, "adada", 26, 1, "PWR", "55-222", 2.00, 3.00, "BARDZS"));
        List<Address> al = aDs.getAllAddresses();
        aDs.close();
        Log.i("Address", "Address id: " + al.get(0).mCity);
        Log.i("Address2", "Address id: " + al.get(1).mCity);
    }

    public void testImages() {
        iDs.open();
        iDs.insertImage(new Image(1, 1, 125, 125, 0, "Ciota",
                "http://tokajiwines.me/photos/no_image.jpg", 1, "Dawno"));
        iDs.insertImage(new Image(2, 1, 125, 125, 0, "Cioty",
                "http://tokajiwines.me/photos/akt1_thumb.jpg", 1, "BDawno"));
        List<Image> il = iDs.getAllImages();
        iDs.close();
        Log.i("Images", "Image id:" + il.get(0).mImage);
        Log.i("Images", "Image id:" + il.get(1).mImage);
    }

    public void testWines() {
        wDs.open();
        List<Wine> wl = null;
        Wine p = wDs.getProducerWineBest(5);
        WineListItem[] d = wDs.getProducerWines(1);
        wDs.close();
        Log.i("Wines", "Wine name:" + wl.get(0).mName);
        Log.i("Wines", "Wine name:" + wl.get(1).mName);
        Log.i("Pierwsze", p.mName);
    }

    public void testWineStrains() {
        wsDs.open();
        wsDs.insertWineStrain(new WineStrain(1, 50, 1, 1));
        wsDs.insertWineStrain(new WineStrain(2, 40, 1, 1));
        List<WineStrain> wsL = wsDs.getAllWineStrains();
        wsDs.close();
        Log.i("Wine Stains", " " + wsL.get(0).mIdStrain_);
        Log.i("Wine Stains", " " + wsL.get(1).mIdStrain_);
    }

    public void testNews() {
        nDs.open();
        nDs.insertNews(new News(1, "Omg", "Omg2", "Start", 1, 1, "Xaxa"));
        nDs.insertNews(new News(2, "Omg", "Omg2", "01.01.2014", "01.09.2014", "Start", 1, 1, "Xaxa"));
        List<News> nL = nDs.getAllNews();
        nDs.close();
        Log.i("News", nL.get(0).mHeaderEng);
        Log.i("News", nL.get(1).mHeaderPl);
    }

    public void testCurrencies() {
        cDs.open();
        cDs.insertCurrency(new Currency(1, "Polski Zloty", "PLN", 1.5f, "Dawno"));
        cDs.insertCurrency(new Currency(2, "Euro", "EUR", 0.5f, "Dawno"));
        List<Currency> cL = cDs.getAllCurrencies();
        cDs.close();
        Log.i("Currencies", cL.get(0).mNameShort);
        Log.i("Currencies", cL.get(1).mNameShort);
    }

    public void testGrades() {
        gDs.open();
        gDs.insertGrade(new Grade(1, "Osioł"));
        gDs.insertGrade(new Grade(2, "Dwor"));
        List<Grade> gL = null;
        gDs.close();
        Log.i("Grades", gL.get(0).mName);
        Log.i("Grades", gL.get(1).mName);
    }

    public void testFlavours() {
        fDs.open();
        fDs.insertFlavour(new Flavour(1, "Soar", "Gorzki"));
        fDs.insertFlavour(new Flavour(2, "Sweet", "Slodki"));
        List<Flavour> fL = null;
        fDs.close();
        Log.i("Flavours", fL.get(0).mNameEng);
        Log.i("Flavours", fL.get(1).mNameEng);
    }

    public void testDescriptions() {
        dDs.open();
        dDs.insertDescription(new Description(1, 1, "xaxaxaaxax", "Ddadada", 1, 1, "Dawno"));
        dDs.insertDescription(new Description(2, 1, "xaxaax", "lololol", 1, 1, "BardzoDawno"));
        List<Description> dL = dDs.getAllDescriptions();
        dDs.close();
        Log.i("Descriptions", dL.get(0).mShort);
        Log.i("Descriptions", dL.get(1).mShort);
    }

    public void runAllTests() {
        testProducers();
        testProducerImages();
        testAddresses();
        testImages();
        testWines();
        testWineStrains();
        testNews();
        testCurrencies();
        testGrades();
        testFlavours();
        testDescriptions();
    }

}
