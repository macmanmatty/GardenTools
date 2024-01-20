
const OpenStreetMap = () => {
  return (
    <div>
      <h2>My OpenStreetMap</h2>
      <iframe
        width="100%"
        height="500"
        frameBorder="0"
        scrolling="no"
        marginHeight="0"
        marginWidth="0"
        src="https://www.openstreetmap.org/export/embed.html?bbox=-74.0586%2C40.4774%2C-73.8262%2C40.9176&layer=mapnik"
      ></iframe>
    </div>
  );
};

export default OpenStreetMap;