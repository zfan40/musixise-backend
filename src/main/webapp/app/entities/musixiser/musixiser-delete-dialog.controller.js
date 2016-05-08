(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('MusixiserDeleteController',MusixiserDeleteController);

    MusixiserDeleteController.$inject = ['$uibModalInstance', 'entity', 'Musixiser'];

    function MusixiserDeleteController($uibModalInstance, entity, Musixiser) {
        var vm = this;
        vm.musixiser = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Musixiser.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
