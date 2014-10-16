
package pl.tokajiwines.fragments;


public class MapOfflineFragment extends BaseFragment {
    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //        mResourceProxy = new ResourceProxyImpl(inflater.getContext().getApplicationContext());
        //        mMapView = new MapView(inflater.getContext(), 256, mResourceProxy);
        //        return mMapView;
        
                final Context context = getActivity();
                final Context applicationContext = context.getApplicationContext();
                final IRegisterReceiver registerReceiver = new SimpleRegisterReceiver(applicationContext);

                // Create a custom tile source
                //        final ITileSource tileSource = new XYTileSource("Mapnik", ResourceProxy.string.mapnik, 1,
                //                18, 256, ".png", "http://tile.openstreetmap.org/");

                // Create a file cache modular provider
                final TileWriter tileWriter = new TileWriter();
                final MapTileFilesystemProvider fileSystemProvider = new MapTileFilesystemProvider(
                        registerReceiver, tileSource);

                // Create an archive file modular tile provider
                GEMFFileArchive gemfFileArchive = GEMFFileArchive.getGEMFFileArchive(mGemfArchiveFilename); // Requires try/catch
                MapTileFileArchiveProvider fileArchiveProvider = new MapTileFileArchiveProvider(
                        registerReceiver, tileSource, new IArchiveFile[] {
                            gemfFileArchive
                        });

                // Create a download modular tile provider
                final NetworkAvailabliltyCheck networkAvailabliltyCheck = new NetworkAvailabliltyCheck(
                        context);
                final MapTileDownloader downloaderProvider = new MapTileDownloader(tileSource, tileWriter,
                        networkAvailablityCheck);

                // Create a custom tile provider array with the custom tile source and the custom tile providers
                final MapTileProviderArray tileProviderArray = new MapTileProviderArray(tileSource,
                        registerReceiver, new MapTileModuleProviderBase[] {
                                fileSystemProvider, fileArchiveProvider, downloaderProvider
                        });

                // Create the mapview with the custom tile provider array
                mMapView = new MapView(context, 256, new DefaultResourceProxyImpl(context),
                        tileProviderArray);
                return new View(context);
                
    }
    */
}
