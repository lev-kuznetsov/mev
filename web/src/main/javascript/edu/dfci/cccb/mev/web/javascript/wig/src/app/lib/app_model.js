(function () {
    "use strict";

    define([], function () {

        return function () {
            var self = this;

            // gene_ontologies.service response
            self.goResponse = {};

            // Pages for page_index.service
            self.pages = [
                {
                    id: 0,
                    location: '/',
                    label: 'glyphicon glyphicon-home',
                    isClass: true // if true, label is treated as span.class attribute
                },
                {
                    id: 1,
                    location: '/upload',
                    label: 'upload',
                    isClass: false // if false, label is treated as span text content
                },
                {
                    id: 2,
                    location: '/select',
                    label: 'select',
                    isClass: false
                },
                {
                    id: 3,
                    location: '/settings',
                    label: 'settings',
                    isClass: false
                }
            ];

        };

    });

}());