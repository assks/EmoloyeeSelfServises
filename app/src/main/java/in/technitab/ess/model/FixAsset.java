package in.technitab.ess.model;

import com.google.gson.annotations.SerializedName;

public class FixAsset {
    @SerializedName("id")
    private int id;
    @SerializedName("type_id")
    private int typeId;
    @SerializedName("asset_type")
    private String assetType;
    @SerializedName("description")
    private String description;
    @SerializedName("make")
    private String make;
    @SerializedName("model")
    private String model;
    @SerializedName("serial_number")
    private String serialNumber;
    @SerializedName("supplier")
    private String supplier;
    @SerializedName("purchase_order_number")
    private String purchaseOrderNumber;
    @SerializedName("invoice_date")
    private String invoiceDate;
    @SerializedName("warranty_period")
    private String warrantyPeriod;
    @SerializedName("delivery_date")
    private String deliveryDate;
    @SerializedName("supplier_invoice_number")
    private String supplierInvoiceNumber;
    @SerializedName("unit_price")
    private String unitPrice;
    @SerializedName("tax_percentage")
    private String taxPercentage;
    @SerializedName("Tax_value")
    private String TaxValue;
    @SerializedName("shipping_tax")
    private String shippingTax;
    @SerializedName("shipping_charge")
    private String shippingCharge;
    @SerializedName("vat_or_gst_percentage")
    private String vatOrGstPercentage;
    @SerializedName("vat_or_gst_value")
    private String vatOrGstValue;
    @SerializedName("total_cost_asset")
    private double totalCostAsset;
    @SerializedName("value_of_asset")
    private String valueOfAsset;
    @SerializedName("class_of_assets")
    private String classOfAssets;
    @SerializedName("useful_life")
    private String usefulLife;
    @SerializedName("depreciation_allowance")
    private String depreciationAllowance;
    @SerializedName("disposal_date")
    private String disposalDate;
    @SerializedName("method_disposal")
    private String methodDisposal;
    @SerializedName("sale_price")
    private String salePrice;
    @SerializedName("accouunts_ref")
    private String accouuntsRef;
    @SerializedName("attachment_path")
    private String attachmentPath;
    @SerializedName("zoho_category")
    private String zohoCategory;
    private boolean isSelected;

    public FixAsset(int id, int typeId, String assetType, String description, String make, String model, String serialNumber, String supplier, String purchaseOrderNumber, String invoiceDate, String warrantyPeriod, String deliveryDate, String supplierInvoiceNumber, String unitPrice, String taxPercentage, String taxValue, String shippingTax, String shippingCharge, String vatOrGstPercentage, String vatOrGstValue, double totalCostAsset, String valueOfAsset, String classOfAssets, String usefulLife, String depreciationAllowance, String disposalDate, String methodDisposal, String salePrice, String accouuntsRef, String attachmentPath, String zohoCategory) {
        this.id = id;
        this.typeId = typeId;
        this.assetType = assetType;
        this.description = description;
        this.make = make;
        this.model = model;
        this.serialNumber = serialNumber;
        this.supplier = supplier;
        this.purchaseOrderNumber = purchaseOrderNumber;
        this.invoiceDate = invoiceDate;
        this.warrantyPeriod = warrantyPeriod;
        this.deliveryDate = deliveryDate;
        this.supplierInvoiceNumber = supplierInvoiceNumber;
        this.unitPrice = unitPrice;
        this.taxPercentage = taxPercentage;
        TaxValue = taxValue;
        this.shippingTax = shippingTax;
        this.shippingCharge = shippingCharge;
        this.vatOrGstPercentage = vatOrGstPercentage;
        this.vatOrGstValue = vatOrGstValue;
        this.totalCostAsset = totalCostAsset;
        this.valueOfAsset = valueOfAsset;
        this.classOfAssets = classOfAssets;
        this.usefulLife = usefulLife;
        this.depreciationAllowance = depreciationAllowance;
        this.disposalDate = disposalDate;
        this.methodDisposal = methodDisposal;
        this.salePrice = salePrice;
        this.accouuntsRef = accouuntsRef;
        this.attachmentPath = attachmentPath;
        this.zohoCategory = zohoCategory;
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return id;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getAssetType() {
        return assetType;
    }

    public String getDescription() {
        return description;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public String getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getSupplierInvoiceNumber() {
        return supplierInvoiceNumber;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public String getTaxPercentage() {
        return taxPercentage;
    }

    public String getTaxValue() {
        return TaxValue;
    }

    public String getShippingTax() {
        return shippingTax;
    }

    public String getShippingCharge() {
        return shippingCharge;
    }

    public String getVatOrGstPercentage() {
        return vatOrGstPercentage;
    }

    public String getVatOrGstValue() {
        return vatOrGstValue;
    }

    public double getTotalCostAsset() {
        return totalCostAsset;
    }

    public String getValueOfAsset() {
        return valueOfAsset;
    }

    public String getClassOfAssets() {
        return classOfAssets;
    }

    public String getUsefulLife() {
        return usefulLife;
    }

    public String getDepreciationAllowance() {
        return depreciationAllowance;
    }

    public String getDisposalDate() {
        return disposalDate;
    }

    public String getMethodDisposal() {
        return methodDisposal;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public String getAccouuntsRef() {
        return accouuntsRef;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public String getZohoCategory() {
        return zohoCategory;
    }
}
