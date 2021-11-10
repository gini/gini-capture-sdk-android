Gini Capture SDK for Android
============================

.. note:: **Deprecation Notice**

    Development of the Gini Capture SDK for Android will be continued in a `new repository
    <https://github.com/gini/gini-mobile-android/tree/main/capture-sdk>`_.

    No new versions will be released from this repository and we kindly ask you to update to the `version
    <https://github.com/gini/gini-mobile-android/releases/tag/capture-sdk%3B1.5.0>`_ released from the new repository.

    A few breaking changes were necessary, but these are easy to fix. You can find the steps in this `migration guide
    <https://github.com/gini/gini-mobile-android/blob/bank-sdk%3B1.4.0/capture-sdk/migrate-from-other-capture-sdk.md>`_.

Introduction
------------

The Gini Capture SDK for Android provides Activities and Fragments to capture documents and prepare them for upload to
the Gini Pay API. It also allows documents to be imported from other apps. The captured images can be reviewed and are
optimized on the device to provide the best results when used with the Gini Pay API. 

The Gini Pay API provides an information extraction service for analyzing documents (e.g. invoices or contracts).
Specifically it extracts information such as the document sender or the payment relevant information (amount to pay,
IBAN, etc.) in an invoice.

Table of contents
-----------------

.. toctree::
    :maxdepth: 2

    getting-started
    integration
    features
    customization-guide
    reference
    license