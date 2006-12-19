class SoftwareRepository {
  private String[] directory
  private Double[] prices

  public SoftwareRepository() {
    directory = ["XFire-1.1", "Groovy-1.O-RC1", "Firefox-1.5"]
    prices = [1.0f, 2.0f, 3.0f]
  }

  public String[] list() {
    return directory
  }

  public Double[] priceList() {
    return prices
  }
}
