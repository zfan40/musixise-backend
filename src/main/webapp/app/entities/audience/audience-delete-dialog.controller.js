(function() {
    'use strict';

    angular
        .module('musixiseApp')
        .controller('AudienceDeleteController',AudienceDeleteController);

    AudienceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Audience'];

    function AudienceDeleteController($uibModalInstance, entity, Audience) {
        var vm = this;
        vm.audience = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Audience.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
